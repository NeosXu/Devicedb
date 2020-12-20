-----------------------------  数据定义  -----------------------------

create type device_status as enum ('onLoan', 'inStock', 'damaged', 'repairing', 'scrapped');
create type request_status as enum ('accepted', 'rejected', 'unhandled');
create type issue_status as enum('repairing', 'scrapped', 'repaired');

CREATE TABLE Account (
    id serial PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    password VARCHAR(30) NOT NULL,
    is_teacher BOOLEAN NOT NULL
);

-- 对于外键，on delete set null不是多余的！
--// admin_id需要写一个默认值0
CREATE TABLE Device(
    id SERIAL PRIMARY KEY,
    name varchar(30) NOT NULL,
    type varchar(30) NOT NULL,
    purchase_time TIMESTAMP NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    producer varchar(30) NOT NULL,
    warranty_until TIMESTAMP NOT NULL,
    transactor_id INT references Account(id) on delete set null,
    status device_status NOT NULL,
    admin_id int not null references Account(id) on delete set default default 0,
    CHECK (warranty_until > purchase_time)
);

create table Issue(
    issue_id serial primary key,
    device_id int not null references Device(id) on delete cascade,
    time timestamp not null,
    reason text,
    expected_days int,
    status issue_status not null default 'repairing'
);

create table Request(
    request_id serial primary key,
    device_id int not null references Device(id) on delete cascade,
    person_id int not null references Account(id) on delete cascade,
    status request_status not null,
    request_date date not null,
    period int not null, 
    reason text
);

-- 添加默认值
create table Lending(
    rec_id serial primary key,
    device_id int not null references Device(id) on delete cascade,
    person_id int not null references Account(id) on delete cascade,
    request_id int references Request(request_id) on delete set null,
    returned BOOLEAN not null default false
);

CREATE FUNCTION transactor_is_not_a_teacher_func() RETURNS TRIGGER AS $$
    BEGIN
        IF (NEW.transactor_id NOT IN (SELECT id FROM Account WHERE is_teacher = true)) THEN
            RAISE EXCEPTION 'transactor is not a teacher';
        ELSE
            RETURN NEW;
        END IF;
    END;
$$ LANGUAGE plpgsql;

CREATE CONSTRAINT TRIGGER transactor_is_not_a_teacher_trig
    AFTER INSERT OR UPDATE OF transactor_id ON Device
    FOR EACH ROW
    EXECUTE FUNCTION transactor_is_not_a_teacher_func();

CREATE FUNCTION admin_is_not_a_teacher_func() RETURNS TRIGGER AS $$
    BEGIN
        IF (NEW.admin_id NOT IN (SELECT id FROM Account WHERE is_teacher = true)) THEN
            RAISE EXCEPTION 'admin is not a teacher';
        ELSE
            RETURN NEW;
        END IF;
    END;
$$ LANGUAGE plpgsql;

CREATE CONSTRAINT TRIGGER admin_is_not_a_teacher_trig
    AFTER INSERT OR UPDATE OF admin_id ON Device
    FOR EACH ROW
    EXECUTE FUNCTION admin_is_not_a_teacher_func();

-----------------------------  函数编写  -----------------------------
-- 1
-- 参数按顺序是Device表中的：name, type, purchase_time, price, producer, warranty_until, transactor_id, admin_id
create or replace function add_device(varchar, varchar, timestamp, double precision, varchar, timestamp, int, int) returns int as $$
begin
	insert into Device values (default, $1, $2, $3, $4, $5, $6, $7, 'inStock', $8);
	return lastval();
end
$$ language plpgsql;

-- 2
-- 第一个参数是设备id，第二个参数是管理员id
create or replace function assign_admin(int, int) returns void as $$
	update Device set admin_id=$2 where id=$1;
$$ language sql;

-- 3
-- 参数是设备id
create or replace function delete_device(int) returns void as $$
	update Request set status='rejected' where device_id=$1;
	delete from Device where id=$1;
$$ language sql;

-- 4
-- 参数是设备id
-- device_damaged改名为set_device_damaged
create or replace function set_device_damaged(int) returns void as $$
	update Device set status='damaged' where id=$1 and status='inStock';
$$ language sql;

-- 5
-- 从第一个参数到最后一个参数是Issue表中的：device_id, time, reason, expected_days
--// repair_device改名为new_issue
create or replace function new_issue(int, timestamp, text, int) returns int as $$
	begin
		if (exists (select * from Device where id=$1 and status='damaged')) then
			update Device set status='repairing' where id=$1;
			insert into Issue values (default, $1, $2, $3, $4, 'repairing');
			return lastval();
		else 
			return null;
		end if;
	end;
$$ language plpgsql;

-- 6
-- 参数是设备id，预估修理时间
-- evaluate_repairing_device改名为evaluate_issue
--// 这个难道不应该是issue_id吗，为什么是device_id
create or replace function evaluate_issue(int, int) returns void as $$
	-- update Issue set expected_days=$2 where device_id=$1;
    update Issue set expected_days=$2 where issue_id=$1;
$$ language sql;

-- 7/8 认定设备报废或者修理好设备
--// 没有提前检查列表中是否存在这个issue，可能导致在列表没有这个issue的情况下还……
--// 但是因为涉及到偶从issue表中查询了设备id的缘故，这个问题可能是不存在的
create or replace function handle_issue(
    in iss_id int,
    in repaired boolean
) returns boolean as $$ 
    begin
        if 'repairing' not in (
            select status from Issue where issue_id=iss_id
        ) then return false;
        else
            if repaired then begin
                update Issue set status='repaired' where issue_id=iss_id;
                update Device set status='inStock' where id=(
                    select device_id from Issue where issue_id=iss_id
                );
                return true;
            end; 
            else begin
            -- 使用with的语法会报错：没有此column
                update Issue set status='scrapped' where issue_id=iss_id;
                update Device set status='scrapped' where id=(
                    select device_id from Issue where issue_id=iss_id
                );
                return true;
            end; end if;
        end if;
    end;
$$ language plpgsql;

-- 9 撤回一个设备维修Issue
create or replace function delete_issue(in id int) returns void as $$
    delete from Issue where issue_id=id;
	update Device set status='damaged' where id=(
		select device_id from Issue where issue_id=id
	);
$$ language sql;

-- 10 清除已经处理的、很久之前的记录
create or replace function clear_handled_issue(in before_time timestamp) returns void as $$
    delete from Issue where time<before_time and status<>'repairing';
$$ language sql;

-- 11 添加一个新的借设备请求
create or replace function new_request(
    dev_id int,
    person_id int,
    date date,
    period int,
    reason text
) returns int as $$
begin
    insert into Request values(default, dev_id, person_id, 'unhandled', date, period, reason);
	return lastval();
end
$$ language plpgsql;

-- 12 拒绝一条设备申请
create or replace function reject_request(req_id int) returns void as $$
    update Request set status='rejected' where request_id=req_id;
$$ language sql;

--13
-- 这个地方有风险，前面的管理员应该是设置成0才对
-- 这个sql语句不成功，子句太长了
CREATE OR REPLACE FUNCTION approve_request(app_id INT, operator_id INT) RETURNS boolean AS $$
BEGIN
IF operator_id = 0 or (operator_id, 'inStock') IN 
    (SELECT admin_id, Device.status FROM Device, Request WHERE request_id = app_id AND device_id = id)
THEN 
BEGIN
  UPDATE Request SET status = 'accepted' WHERE request_id = app_id;
  update Device set status='onLoan' where id=(
    select device_id from Request where request_id=app_id
  );
  INSERT INTO Lending(device_id, person_id, request_id)
    (SELECT device_id, person_id, request_id FROM Request WHERE request_id = app_id);
  return true;
END;
END IF;
return false;
END;
$$ LANGUAGE plpgsql;

--14
CREATE OR REPLACE FUNCTION return_device(dev_id INT) RETURNS VOID AS $$
BEGIN
UPDATE Lending SET returned = TRUE WHERE dev_id = dev_id AND returned = FALSE;
UPDATE Device SET status = 'inStock' WHERE dev_id = dev_id AND status = 'onLoan';
END;
$$ LANGUAGE plpgsql;

--15
CREATE FUNCTION delete_requests(t DATE) RETURNS VOID AS $$
BEGIN
DELETE FROM Request WHERE request_date < t;
END;
$$ LANGUAGE plpgsql;

--16
CREATE OR REPLACE FUNCTION create_account(uname VARCHAR(20), pwd VARCHAR(30), teacher BOOLEAN) RETURNS int AS $$
BEGIN
    INSERT INTO Account(name, password, is_teacher) VALUES(uname, pwd, teacher);
    return lastval();
END;
$$ LANGUAGE plpgsql;

--17
-- 规定Admin的ID是0，不可更改
create or replace function delete_account(uid int) returns void as $$
begin
    if uid<>0 then begin
        delete from Account where id = uid;
        update Device set admin_id = 0 where id in
            (select Device.id 
            from Device, Account 
            where uid= Account.id and admin_id = Account.id);
    end;
    end if;
end;
$$ language plpgsql;

--18
CREATE FUNCTION change_password(uid int, npwd VARCHAR(30)) RETURNS VOID AS $$
BEGIN
UPDATE Account SET password = npwd WHERE id = uid;
END;
$$ LANGUAGE plpgsql;

-- 初始化管理员
insert into account values(0, 'admin', 'admin', true);

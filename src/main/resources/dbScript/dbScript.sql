drop database if exists sanasa;
create database sanasa;
use sanasa;

/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
create table member(
	memId int,
	name varchar(100) not null,
	address varchar(100) not null,
	dob date not null,
	nic char(10) not null,
	contact char(10) not null,
	gender boolean not null,              /* true=male,  false=female */    
	isMember boolean not null,
	isActive boolean not null,
	isApplicant boolean not null,
	parentId  int not null,
	constraint primary key(memId)
)engine=innodb;

insert into member value (1 ,'Kamala Sirisena'  ,'No:33,Horana Rd,Bandaragama'  ,'1989-05-11' ,'963420706V' ,'0713456789' ,false ,true  ,true ,false ,0 );
insert into member value (2 ,'Dayani Perera'    ,'No:63/1,Temple Rd,Halthota'   ,'1972-03-04' ,'963678706V' ,'0778848825' ,false ,true  ,true ,false ,1 );
insert into member value (3 ,'Pasindu Harshana' ,'No:12/8,Walgama Rd,Weedagama' ,'1995-10-25' ,'789420706V' ,'0716782695' ,true  ,true  ,true ,false ,1 );
insert into member value (4 ,'Wasantha Kumari'  ,'No:62/5,Rerukana Rd,Gonapala' ,'1975-03-21' ,'873422396V' ,'0750984683' ,false ,false ,true ,true  ,2 );
insert into member value (5 ,'Ruwan Pathirana'  ,'No:22/1,Shanthi Rd,Wevita'    ,'1964-08-26' ,'623440706V' ,'0712461298' ,true  ,false ,true ,false ,-1);

/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
create table memImage(
	memId int,
	image MEDIUMBLOB,
	constraint primary key(memId)
)engine=innodb;

insert into memImage value (1 ,null );
insert into memImage value (2 ,null );
insert into memImage value (3 ,null );
insert into memImage value (4 ,null );
insert into memImage value (5 ,null );
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
create table account(
	accId int,
	memId int not null,
	norBalance double not null,
	comBalance double,
	constraint primary key(accId),
	constraint foreign key(memId) references member(memId)
)engine=innodb;

insert into account value (1 ,1 ,1134.54 ,6500 );
insert into account value (2 ,2 ,2000.00 ,4750 );
insert into account value (3 ,3 ,1200.00 ,8310 );
insert into account value (4 ,4 ,2450.00 ,null );
insert into account value (5 ,5 ,350.00  ,null );

/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table normal(
	norId int,
	accId int not null,
	norDate date not null,
	norTime time not null,
	norType int not null,    /* 0=deposit, 1=interest, 2=withdraw */
	amount double not null,
	balance double not null,
	interest double not null,
	constraint primary key(norId),
	constraint foreign key(accId) references account(accId)
)engine=innodb;

insert into normal value (1 ,1 ,'2002-11-03' ,'10:00:11' ,0 , 500.00 , 500.00 ,0.00 );
insert into normal value (2 ,1 ,'2003-01-14' ,'09:45:37' ,0 , 600.00 ,1100.00 ,0.00 );
insert into normal value (3 ,2 ,'2003-03-23' ,'14:12:56' ,0 ,3000.00 ,3000.00 ,0.00 );
insert into normal value (4 ,1 ,'2003-05-26' ,'16:23:21' ,1 ,  34.54 ,1134.54 ,0.00 );
insert into normal value (5 ,3 ,'2004-02-19' ,'11:37:37' ,0 ,1200.00 ,1200.00 ,0.00 );
insert into normal value (6 ,4 ,'2004-04-27' ,'09:58:00' ,0 ,2450.00 ,2450.00 ,0.00 );
insert into normal value (7 ,2 ,'2004-05-09' ,'12:09:25' ,2 ,1000.00 ,2000.00 ,0.00 );
insert into normal value (8 ,5 ,'2004-06-11' ,'15:13:44' ,0 , 350.00 , 350.00 ,0.00 );
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table compulsory(
	comId int,
	accId int not null,
	comDate date not null,
	comTime time not null,
	comType int not null,    /* 0=deposit, 1=interest */
	amount double not null,
	balance double not null,
	interest double not null,
	constraint primary key(comId),
	constraint foreign key(accId) references account(accId)
)engine=innodb;

insert into compulsory value (1 ,1 ,'2002-10-03' ,'11:00:11' ,0 , 500.00 , 500.00 ,0.00 );
insert into compulsory value (2 ,1 ,'2002-02-14' ,'08:46:38' ,0 , 700.00 ,1200.00 ,0.00 );
insert into compulsory value (3 ,2 ,'2003-02-26' ,'15:14:56' ,0 , 450.00 , 450.00 ,0.00 );
insert into compulsory value (4 ,1 ,'2003-05-26' ,'13:26:21' ,1 ,  78.89 ,1278.89 ,0.00 );
insert into compulsory value (5 ,3 ,'2003-02-12' ,'10:41:37' ,0 ,1200.00 ,1200.00 ,0.00 );
insert into compulsory value (6 ,2 ,'2004-04-27' ,'09:03:08' ,0 ,2450.00 ,2900.00 ,0.00 );
insert into compulsory value (7 ,2 ,'2004-05-11' ,'12:09:25' ,1 ,  56.87 ,2956.87 ,0.00 );
insert into compulsory value (8 ,3 ,'2005-06-23' ,'14:17:31' ,0 , 350.00 ,1550.00 ,0.00 );	
	 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table fixed(
	fixId int,
	accId int not null,
	fixDate date not null,
	fixTime time not null,
	period int not null,           /* period unit 1 = 1 month */
	amount double not null,
	isMaturity boolean not null,
	isWithdraw boolean not null,
	isBond boolean not null,
	interest double,
	constraint primary key(fixId),
	constraint foreign key(accId) references account(accId)
)engine=innodb;

insert into fixed value (1 ,1 ,'2016-08-03' ,'13:00:11' ,12 ,100000.00 ,false ,false ,false ,null    );
insert into fixed value (2 ,5 ,'2016-08-06' ,'09:21:23' ,3  , 50000.00 ,true  ,true  ,false ,4537.67 );
insert into fixed value (3 ,3 ,'2016-05-27' ,'11:45:32' ,9  , 75000.00 ,false ,true  ,false ,7879.34 );
insert into fixed value (4 ,2 ,'2016-09-15' ,'09:23:45' ,18 ,100000.00 ,false ,true  ,false ,7879.34 );
insert into fixed value (5 ,4 ,'2016-05-03' ,'12:45:31' ,18 ,100000.00 ,false ,true  ,false ,7879.34 );
insert into fixed value (6 ,1 ,'2016-08-22' ,'15:32:59' ,12 ,200000.00 ,true  ,false ,true  ,null    );
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table loan(
	loanId int,
	loanName varchar(100) not null,
	rate double not null,
	maxAmount double,
	forMember boolean not null,	       /* true=for only members, false=for both*/
	method boolean not null,           /* true=redusing balance method, false=compound interest method*/
	rateType int not null,             /* 0=daily, 1=monthly, 2=annually, */
	fineType int not null,             /* 0=nonType, 1=costType, 2=rateType */
	guaranteeType int not null,        /* 0=none, 1=personal, 2=property, 3=fix*/
	minPeriod int,                     /* period unit 1 = 1 month */
	maxPeriod int,	                   /* period unit 1 = 1 month */
	constraint primary key(loanID)
)engine=innodb;

insert into loan value (1 ,'Member Loan'     ,13.00 ,100000 ,true  ,true  ,2 ,2 ,1 ,4    ,30   );
insert into loan value (2 ,'Property Loan'   ,16.00 ,400000 ,true  ,true  ,2 ,0 ,2 ,8    ,60   );
insert into loan value (3 ,'Short Time Loan' , 3.00 ,50000  ,true  ,true  ,1 ,0 ,2 ,1    ,6    );
insert into loan value (4 ,'Quick Loan'      , 3.00 ,25000  ,true  ,false ,1 ,0 ,1 ,1    ,6    );
insert into loan value (5 ,'Fixed Bond Loan' ,13.00 ,null   ,false ,true  ,2 ,0 ,3 ,4    ,12   );
insert into loan value (6 ,'Festival Loan'   ,10.00 ,20000  ,true  ,false ,2 ,1 ,1 ,null ,null );	
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table loanTaken(
	takenId int,
	memId int not null,
	loanName varchar(100) not null,
	method boolean not null,            /* true=redusing balance method, false=compound interest method*/
	rate double not null,
	rateType int not null,              /* 0=daily, 1=monthly, 2=annually, */
	fineType int not null,              /* 0=nonType, 1=costType, 2=rateType */
	guaranteeType int not null,         /* 0=none, 1=personal, 2=property, 3=fix*/	
	takenDate date  not null,
	takenTime time not null,
	period int,                         /* period unit 1 = 1 month */
	amount double not null,
	balance double not null,
	constraint primary key(takenID),
	constraint foreign key(memId) references member(memId)
)engine=innodb;

insert into loanTaken value (1 ,2 ,'Fixed Bond Loan' ,true  ,13 ,2 ,0 ,3 ,'2006-09-15' ,'15:18:45' ,12 ,90000  ,45670    );
insert into loanTaken value (2 ,5 ,'Fixed Bond Loan' ,true  ,13 ,2 ,0 ,3 ,'2006-11-03' ,'08:57:49' ,12 ,75000  ,63400    );
insert into loanTaken value (3 ,2 ,'Member Loan'     ,true  ,13 ,2 ,2 ,1 ,'2008-02-23' ,'10:23:45' ,20 ,75000  ,546780.57);
insert into loanTaken value (4 ,3 ,'Festival Loan'   ,false ,10 ,2 ,1 ,1 ,'2008-04-02' ,'09:34:22' ,10 ,15000  ,15000    );
insert into loanTaken value (5 ,1 ,'Property Loan'   ,true  ,16 ,2 ,0 ,2 ,'2009-11-14' ,'14:56:45' ,36 ,350000 ,234570   );
insert into loanTaken value (6 ,3 ,'Short Time Loan' ,true  ,3  ,1 ,0 ,2 ,'2010-05-25' ,'12:43:11' ,20 ,50000  ,34780.38 );	
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table payment(
	payId int,
	takenId int not null,
	payDate date not null,
	payTime time not null,
	amount double not null,
	fine double,
	interest double,
	constraint primary key(payId),
	constraint foreign key(takenId) references loanTaken(takenID)
)engine=innodb;

insert into payment value (1 ,1 ,'2006-10-16' ,'12:12:21' ,8000.00 ,null ,975.00 );
insert into payment value (2 ,2 ,'2006-12-05' ,'08:33:36' ,6500.00 ,null ,812.50 );
insert into payment value (3 ,1 ,'2006-11-20' ,'09:56:11' ,8200.00 ,20   ,898.80 );
insert into payment value (4 ,2 ,'2007-01-04' ,'08:41:21' ,6700.00 ,null ,750.80 );
insert into payment value (5 ,1 ,'2006-12-14' ,'10:56:34' ,8000.00 ,null ,813.40 );
insert into payment value (6 ,2 ,'2007-02-16' ,'12:49:56' ,6500.00 ,35   ,692.50 );
insert into payment value (7 ,3 ,'2008-03-23' ,'11:29:21' ,4000.00 ,null ,812.50 );
insert into payment value (8 ,3 ,'2008-04-20' ,'12:07:05' ,4000.00 ,null ,777.90 );		 
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table guarantor(
	guaId int,
	takenId int not null,
	memId int not null,
	constraint primary key(guaId),
	constraint foreign key(takenId) references loanTaken(takenID)
)engine=innodb;

insert into guarantor value (1 ,3 ,1 );
insert into guarantor value (2 ,3 ,2 );
insert into guarantor value (3 ,6 ,2 );
insert into guarantor value (4 ,6 ,3 );
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table shares(
	shaId int,
	memId int not null,
	shaDate date not null,
	shaTime time not null,
	amount double not null,
	constraint primary key(shaId),
	constraint foreign key(memId) references member(memId)
)engine=innodb;

insert into shares value (1 ,1 ,'2002-10-24' ,'08:12:15' ,1500.00 );
insert into shares value (2 ,1 ,'2003-05-13' ,'09:56:16' ,750.00  );
insert into shares value (3 ,2 ,'2002-12-07' ,'08:31:22' ,2000.00 );
insert into shares value (4 ,3 ,'2003-09-24' ,'10:07:45' ,4000.00 );
insert into shares value (5 ,3 ,'2006-11-17' ,'08:23:15' ,300.00  );	 

/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table attendance(
	attId int,
	memId int not null,
	meetDate date not null,
	status boolean not null,
	constraint primary key(attId),
	constraint foreign key(memId) references account(memId)
)engine=innodb;

insert into attendance value (1 ,1 ,'2002-10-27' ,true  );
insert into attendance value (2 ,2 ,'2003-05-25' ,true  );
insert into attendance value (3 ,2 ,'2003-05-25' ,false );
insert into attendance value (4 ,1 ,'2003-09-30' ,true  );
insert into attendance value (5 ,2 ,'2003-09-30' ,false );
insert into attendance value (6 ,3 ,'2006-11-27' ,true  );		 
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table users(
	userName varchar(100) not null,
    userPassword varchar(8) not null,
    userType int not null                /* 0=admin, 1=clerk, 2=audit, */
)engine=innodb;

insert into users value ('z' ,'z' ,0 );
insert into users value ('x' ,'x' ,1 );
insert into users value ('c' ,'c' ,2 );
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
create table detail(
	detailId int,
    description varchar(100) not null,
    detail double not null,
	constraint primary key(detailId)
)engine=innodb;

insert into detail value (1 ,'Normal Deposit Rate'        ,7.00   );
insert into detail value (2 ,'Compulsory Deposit Rate'    ,9.00   );
insert into detail value (3 ,'Fixed Deposit Rate'         ,10.00  );
insert into detail value (4 ,'Minimum Interest Day Count' ,30.00  );
insert into detail value (5 ,'Minimum Interest Amount'    ,300.00 );
		 
/* ---------------------------------------------------------------------------------------------------------------------------------------------------------------- */		 
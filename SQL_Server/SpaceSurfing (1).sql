DROP TABLE userAccount CASCADE CONSTRAINTS;
DROP TABLE housingfeed CASCADE CONSTRAINTS;
DROP TABLE post CASCADE CONSTRAINTS;
DROP TABLE livingSpace CASCADE CONSTRAINTS;
DROP TABLE sublessor CASCADE CONSTRAINTS;
DROP TABLE renter CASCADE CONSTRAINTS;

    CREATE TABLE userAccount (
      userid        number(20) not null, 
      objid         number(20),
      username      varchar2(50) not null,
      userclass     number(4),
      usermajor     varchar2(50),
      renterid      number(20),
      sublessorid   number(20),
      gender        char,
      email         varchar(50),
      phonenumber   number(10),
      address       varchar2(100),
      profilephoto  blob,
      smoker        number(1),
      drinker       number(1),
      drug          number(1),
      party         number(1),
      car           number(1),
      hobby         varchar2(100),
      primary key (userid)
      
    );
    
    CREATE TABLE housingfeed (
      postid        number(20) not null,
      userid        number(20) not null,
      created_at    date not null, 
      updated_at    date,
      primary key (postid)
      
    );
    
    CREATE TABLE post (
      postid        number(20) not null,
      spaceid       number(20) not null,
      author        number(20) not null,
      title         varchar2(50),
      bodytext      varchar2(400),
      image         blob,
      durationTime  number(30), 
      created_at    date not null,
      updated_at    date,
      likes         number(10) not null,
      comments      number(10) not null,
      shares        number(10) not null,
      primary key (postid)
      
    );
    
    CREATE TABLE livingSpace (
        spaceid             number(20) not null,
        photo               blob,
        linkToHousing       varchar2(100),
        price               number(20,2) not null,
        landlordName        varchar2(80),
        landlordContactNum  number(10),
        address             varchar2(100) not null,
        availablePeriod     date,
        housingType         varchar2(50) not null,
        bedroom             number(5),
        confirmedRoommates  number(5),
        totalRoommates      number(5),
        furnished           number(1),
        parkinglot          number(1),
        toilet              number(1),
        laundry             number(1),
        pool                number(1),
        grocery             number(1),
        primary key (spaceid)
        
    );
    
    CREATE TABLE sublessor (
        sublessorid     number(20) not null,
        spaceid         number(20) not null,
        primary key(sublessorid),
        foreign key (spaceid) references livingSpace(spaceid)
    );
    
    CREATE TABLE renter (
        renterid        number(20) not null,
        spaceid         number(20) not null,
        primary key(renterid),
        foreign key (spaceid) references livingSpace(spaceid)
    );
    
    ALTER TABLE userAccount ADD(      
    foreign key (objid)
    references post(postid)
    );
    
    ALTER TABLE userAccount ADD (
    foreign key (sublessorid)
    references sublessor(sublessorid)
    );
    
    ALTER TABLE userAccount ADD (
    foreign key (renterid)
    references renter(renterid)
    );
    
    ALTER TABLE housingfeed ADD (      
    foreign key (userid)
    references userAccount(userid)
    );
    
    ALTER TABLE post ADD (
    foreign key (author)
    references userAccount(userid)
    );
    
    ALTER TABLE post ADD (       
    foreign key (spaceid)
    references livingSpace(spaceid)
    );


                                

                                


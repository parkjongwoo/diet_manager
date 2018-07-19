------------------------------------로그인 화면

--***로그인 화면 id,pw 검색 결과 쿼리:: 로그인 유저의 최근 몸무게를 포함하여 신상정보를 반환.
--직접입력
SELECT a.aname name, a.azender gender, a.abirth birth, a.aheight height,
			    w.aweight weight, a.aactive active
			    FROM d_acount a INNER JOIN 
                (select * from d_weight where aid='hong' and adate in
                (select max(adate) from d_weight where aid='hong')) w 
			    ON a.aid=w.aid 
			    WHERE a.aid='hong' and a.apass='1c73d5362e054cfee78a7e530f59f5faf5457fe970b599effc34dfd968b4119b';

--java용:: 반환 값이 있으면 로그인 성공. 없으면 실패.
--SELECT a.aname name, a.azender gender, a.abirth birth, a.aheight height,
--			    w.aweight weight, a.aactive active
--			    FROM d_acount a INNER JOIN 
--                (select * from d_weight where aid=? and adate in
--                (select max(adate) from d_weight where aid=?)) w 
--			    ON a.aid=w.aid 
--			    WHERE a.aid=? and a.apass=?;


--***회원가입 화면 정보 입력 쿼리
--직접입력
INSERT ALL
INTO d_acount VALUES (aid, aname, apass, azender, abirth, aheight, aactive)
INTO d_weight VALUES (aid, adate, aweight)
SELECT 'emm2000' aid, '음바페' aname,'1c73d5362e054cfee78a7e530f59f5faf5457fe970b599effc34dfd968b4119b' apass,
'남성' azender, to_char(to_date('2002-09-05-14-11-12', 'yyyy-mm-dd-hh24-mi-ss'),'yyyy-mm-dd') abirth, 190 aheight, 30 aactive,
TO_CHAR(SYSDATE,'yyyy-mm-dd') adate, 88 aweight from dual;
--java용
--INSERT ALL
--INTO d_acount VALUES (aid, aname, apass, azender, abirth, aheight, aactive)
--INTO d_weight VALUES (aid, adate, aweight)
--SELECT ? aid, ? aname,? apass,
--? azender, to_char(?,'yyyy-mm-dd') abirth, ? aheight, ? aactive,
--TO_CHAR(SYSDATE,'yyyy-mm-dd') adate, ? aweight from dual;


--------------------------------------상세보기 화면

--***날짜로 식단 조회: 칼로리 포함 각 영양성분은 1회제공량 기준이므로 주의:: 어플의 영양성분 산출식은 EINTAKE(먹은량) / FPER(1회제공량) * (각 영양성분값) ::  
--직접입력
SELECT F.FNAME FNAME, E.EINTAKE EINTAKE, F.FPER FPER, F.FCAL FCAL, F.FCO FCO, F.FPRO FPRO, F.FFAT FFAT, F.FSU FSU, F.FNA FNA, F.FCHO FCHO, F.FSAT FSAT, F.FTRAN FTRAN
FROM D_EAT E INNER JOIN D_FOOD F ON E.FID=F.FID 
WHERE EDATE='2018/07/18' AND E.AID='shoong1999'
ORDER BY ETIME DESC, EDATE ASC;

--JAVA용
SELECT F.FNAME FNAME, E.EINTAKE EINTAKE, F.FPER FPER, F.FCAL FCAL, F.FCO FCO, F.FPRO FPRO, F.FFAT FFAT, F.FSU FSU, F.FNA FNA, F.FCHO FCHO, F.FSAT FSAT, F.FTRAN FTRAN
FROM D_EAT E INNER JOIN D_FOOD F ON E.FID=F.FID 
WHERE EDATE=? AND E.AID=?
ORDER BY ETIME DESC, EDATE ASC;--아침,점심,저녁 순 정렬, 입력순서로 다시 정렬


--*** 사용자 프로필 수정
--직접입력
UPDATE D_ACOUNT SET
AACTIVE=20
WHERE AID='shoong1999';
--java용
--UPDATE D_ACOUNT SET
--AACTIVE=?
--WHERE AID=?;


--*** 사용자 프로필 검색
--직접입력
SELECT * FROM D_ACOUNT WHERE AID='shoong1999';
--java용
SELECT * FROM D_ACOUNT WHERE AID=?;
-------------------------------------입력화면

--***입력할 식단 검색

--직접입력
SELECT * FROM D_FOOD WHERE FNAME LIKE '%울면%';
--JAVA용 :: 단어 포함 LIKE 검색 이므로 ?자리에 %검색어% 형식으로 넣어 줄 것...
SELECT * FROM D_FOOD WHERE FNAME LIKE ?;

--***새 식단 입력
--직접입력
INSERT INTO D_EAT(EID,AID,EDATE,ETIME,FID,EINTAKE)
VALUES (D_SEQ_EID.NEXTVAL, 'shoong1999', '2018/07/18', '아침', 5615, 500);

--java용
--INSERT INTO D_EAT(EID,AID,EDATE,ETIME,FID,EINTAKE)
--VALUES (D_SEQ_EID.NEXTVAL, ?, ?, ?, ?, ?);


--***아침,점심,저녁별 식단표 검색
--직접입력
--날짜 및 아침로 조회: 칼로리 포함 각 영양성분은 1회제공량 기준이므로 주의:: 어플의 영양성분 산출식은 EINTAKE(먹은량) / FPER(1회제공량) * (각 영양성분값) ::  
SELECT E.EID EID, E.AID AID, E.EDATE EDATE, E.ETIME ETIME, E.FID FID, E.EINTAKE EINTAKE,
				 F.FNAME FNAME, F.FPER FPER, F.FCAL FCAL, F.FCO FCO, F.FPRO FPRO, 
				 F.FFAT FFAT, F.FSU FSU, F.FNA FNA, F.FCHO FCHO, F.FSAT FSAT, F.FTRAN FTRAN 
				FROM D_EAT E INNER JOIN D_FOOD F ON E.FID=F.FID 
				WHERE TO_CHAR(EDATE,'YYYY-MM-DD')=TO_CHAR(?,'YYYY-MM-DD') AND E.AID='shoong1999' AND E.ETIME='아침'
				ORDER BY EDATE ASC;

--JAVA용
--날짜 및 아침로 조회: 칼로리 포함 각 영양성분은 1회제공량 기준이므로 주의:: 어플의 영양성분 산출식은 EINTAKE(먹은량) / FPER(1회제공량) * (각 영양성분값) ::  
SELECT E.EID EID, E.AID AID, E.EDATE EDATE, E.ETIME ETIME, E.FID FID, E.EINTAKE EINTAKE,
				 F.FNAME FNAME, F.FPER FPER, F.FCAL FCAL, F.FCO FCO, F.FPRO FPRO, 
				 F.FFAT FFAT, F.FSU FSU, F.FNA FNA, F.FCHO FCHO, F.FSAT FSAT, F.FTRAN FTRAN 
				FROM D_EAT E INNER JOIN D_FOOD F ON E.FID=F.FID 
				WHERE TO_CHAR(EDATE,'YYYY-MM-DD')=TO_CHAR(?,'YYYY-MM-DD') AND E.AID=? AND E.ETIME=?
				ORDER BY EDATE ASC;

---------------------------------------------메인화면

--***사용자 오늘날짜 몸무게가 있으면 업데이트, 없으면 인서트 쿼리
--직접입력
MERGE INTO d_weight w
USING (SELECT 'hong' aid,TO_CHAR(SYSDATE,'yyyy-mm-dd') adate,100 aweight FROM dual) n
ON ( w.aid = n.aid and w.adate=n.adate)
WHEN MATCHED THEN
UPDATE
SET w.aweight = n.aweight
WHEN NOT MATCHED THEN
INSERT ( aid,adate,aweight)
VALUES (n.aid,n.adate,n.aweight);

--java용
--***사용자 오늘날짜 몸무게가 있으면 업데이트, 없으면 인서트 쿼리:: 첫?- 사용자 id :: 두번째?-입력할 몸무게
MERGE INTO d_weight w
USING (SELECT ? aid,TO_CHAR(SYSDATE,'yyyy-mm-dd') adate,? aweight FROM dual) n
ON ( w.aid = n.aid and w.adate=n.adate)
WHEN MATCHED THEN
UPDATE
SET w.aweight = n.aweight
WHEN NOT MATCHED THEN
INSERT ( aid,adate,aweight)
VALUES (n.aid,n.adate,n.aweight);


--***시간대별 몸무게 정보
SELECT ADATE,AWEIGHT FROM D_WEIGHT WHERE AID=?;
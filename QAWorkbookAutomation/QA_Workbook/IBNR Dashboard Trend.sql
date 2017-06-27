BEGIN TRY
	DROP TABLE #Temp_Population_NoofMonths		
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY
	DROP TABLE #Temp_IBNR_ResultData
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY
	DROP TABLE #Temp_IBNR_FinalResultData
END TRY
BEGIN CATCH
END CATCH


CREATE TABLE #Temp_Population_NoofMonths(RNo Int Identity(1,1)
,[Population] Varchar(Max)
,LastValidDate Date
,LastClaimDate Date
,StartDate Date
,EndDate Date
,NoofMonths int
)

INSERT INTO #Temp_Population_NoofMonths(
[Population]
,LastValidDate
,LastClaimDate
,StartDate
,EndDate
,NoofMonths
)
SELECT SubPopulation 
,LastValidDate
,LastClaimDate
,DATEADD(MONTH,-12,LastValidDate)
,DATEADD(MONTH,-1,LastValidDate)
,DATEDIFF(MONTH,LastValidDate,LastClaimDate)+13
FROM ABCR.PopulationDates

CREATE TABLE #Temp_IBNR_ResultData(
[Population] Varchar(Max)
,Period  Varchar(Max)
,[IBNR Incurred Allowed] numeric(18,2)
,[IBNR Incurred Paid] numeric(18,2)
,[IBNR Amount Allowed - PaidClaims] numeric(18,2)
,[IBNR Amount Paid - PaidClaims] numeric(18,2)
)

CREATE TABLE #Temp_IBNR_FinalResultData(
[Population] Varchar(Max)
,[Display Name] Varchar(Max)
,[Cost Basis] Varchar(Max)
,Period  Varchar(Max)
,[Incurred Claims] Numeric(18,2)
,[Paid Claims] Numeric(18,2)
)

DECLARE @NoofPopulations int,
@MaxPeriod int,
@Year Varchar(4),
@i int = 1,
@RNo int = 1 

SELECT @NoofPopulations=count(*) FROM #Temp_Population_NoofMonths

WHILE (@RNo<=@NoofPopulations)
BEGIN

SELECT @MaxPeriod=DATEPART(MONTH,MAX(LastClaimDate)) From #Temp_Population_NoofMonths
WHERE RNo=@RNo

WHILE (@i<=@MaxPeriod)
BEGIN 

SELECT @Year=YEAR(MAX(LastClaimDate)) From #Temp_Population_NoofMonths
WHERE RNo=@RNo

INSERT INTO #Temp_IBNR_ResultData(
[Population]
,Period
)
SELECT [Population]
,DATEADD(MONTH,@i-1,CAST(@Year+'-01-01' AS DATE)) 'Period'
FROM #Temp_Population_NoofMonths
WHERE RNo=@RNo
GROUP BY [Population]

UPDATE tir
SET tir.[IBNR Incurred Allowed]=tir2.[Incurred Allowed],
tir.[IBNR Incurred Paid]=tir2.[Incurred Paid],
tir.[IBNR Amount Allowed - PaidClaims]=tir2.[Amount Allowed - PaidClaims],
tir.[IBNR Amount Paid - PaidClaims]=tir2.[Amount Paid - PaidClaims]
FROM #Temp_IBNR_ResultData tir
INNER JOIN
(
SELECT
[Population]
,SUM([Incurred Allowed]) 'Incurred Allowed'
,SUM([Incurred Paid]) 'Incurred Paid'
,SUM([Amount Allowed - PaidClaims]) 'Amount Allowed - PaidClaims'
,SUM([Amount Paid - PaidClaims]) 'Amount Paid - PaidClaims' 
FROM 
(
SELECT
	_MI_USER_DIM_07_ 'Population',
	SERVICE_MONTH_START_DATE 'Period',
	SUM(amt_allowed_w_ibnr) 'Incurred Allowed',
	SUM(amt_paid_w_ibnr) 'Incurred Paid',
	SUM(amt_allowed) 'Amount Allowed - PaidClaims',
	SUM(amt_paid) 'Amount Paid - PaidClaims'
FROM dbo.IBNR_HISTORY h
INNER JOIN RFT_MI_USER_DIM_07_ pop
	ON h._MI_USER_DIM_07_KEY = pop._MI_USER_DIM_07_KEY
WHERE h.MI_POST_DATE = (SELECT
	MAX(MI_POST_DATE)
FROM dbo.IBNR_HISTORY)
AND YEAR(SERVICE_MONTH_START_DATE) = @Year
AND (h.amt_paid IS NOT NULL
OR h.amt_allowed IS NOT NULL)
GROUP BY _MI_USER_DIM_07_, SERVICE_MONTH_START_DATE
) IBNR
WHERE Period<=DATEADD(MONTH,@i-1,CAST(@Year+'-01-01' AS DATE))
GROUP BY IBNR.[Population]
) tir2
ON tir.[Population]=tir2.[Population]
AND tir.Period=DATEADD(MONTH,@i-1,CAST(@Year+'-01-01' AS DATE))

SET @i=@i+1
END
SET @RNo=@RNo+1
SET @i=1
END

INSERT INTO #Temp_IBNR_FinalResultData
(
[Population]
,[Cost Basis]
,Period
,[Incurred Claims]
,[Paid Claims]
)
SELECT [Population]
,'Allowed'
,Period
,[IBNR Incurred Allowed]
,[IBNR Amount Allowed - PaidClaims]
FROM #Temp_IBNR_ResultData
UNION
SELECT [Population]
,'Paid'
,Period
,[IBNR Incurred Paid] 
,[IBNR Amount Paid - PaidClaims]
FROM #Temp_IBNR_ResultData

UPDATE tr
SET tr.[Display Name]=ap.DisplayName
FROM #Temp_IBNR_FinalResultData tr
INNER JOIN ABCR.PopulationDates ap ON tr.Population=ap.SubPopulation

SELECT * FROM #Temp_IBNR_FinalResultData
ORDER BY Population,[Cost Basis],Period


BEGIN TRY
	DROP TABLE #Temp_Population_NoofMonths		
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY
	DROP TABLE #Temp_IBNR_ResultData
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY
	DROP TABLE #Temp_IBNR_FinalResultData
END TRY
BEGIN CATCH
END CATCH
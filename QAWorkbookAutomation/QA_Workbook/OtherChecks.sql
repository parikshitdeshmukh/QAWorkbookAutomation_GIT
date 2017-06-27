SET NOCOUNT ON

BEGIN TRY

Drop TABLE #Temp_StgvsPrdComparison

END TRY
BEGIN CATCH
END CATCH

BEGIN TRY

Drop TABLE #Temp_IncompleteMonths

END TRY
BEGIN CATCH
END CATCH

CREATE Table #Temp_StgvsPrdComparison
( 
Population Varchar(Max),
DisplayName Varchar(Max),
Tabs Varchar(Max),
Checks Varchar(Max),
Results Varchar(max)
)

SELECT * INTO #Temp_IncompleteMonths
FROM (
select pop._mi_user_dim_07_ 'Population'
, h.SERVICE_MONTH_START_DATE 'Period'
, (h.amt_paid /h. amt_paid_w_ibnr)*100 'PercentComplete'
,DATENAME(MONTH,h.service_month_start_date)+'-'+RIGHT(DATENAME(YEAR,h.service_month_start_date),2) 
+'('+Cast(Round(Cast((h.amt_paid /h. amt_paid_w_ibnr)*100 as decimal(2)),2) AS varchar(max))+'%)' 'PeriodAndPercentage'
from IBNR_History h
      left join RFT_MI_User_Dim_07_ pop on pop ._mi_user_dim_07_key = h._mi_user_dim_07_key
where h. MI_POST_DATE = (select MAX(mi_post_date ) from IBNR_HISTORY where _MI_USER_DIM_07_KEY = h._MI_USER_DIM_07_KEY )
and h. amt_paid_w_ibnr <> 0 
and h.amt_paid is not null
) a
where a.PercentComplete<94
AND a.[Population] IS NOT NULL
ORDER by a.Population,a.Period

Insert INTO #Temp_StgvsPrdComparison (Population,Tabs,Checks,Results)
SELECT Main.[Population],'Focus','No of Incomplete Months',LEFT(Main.Incompletemonths,Len(Main.Incompletemonths)-2) as 'Incomplete Months'
From
(
select DISTINCT t2.[Population], 
(
	SELECT t1.PeriodAndPercentage + ', ' AS [text()]
	FROM #Temp_IncompleteMonths t1
	WHERE t1.[Population]=t2.[Population]
	ORDER BY t1.[Population]
	FOR XML PATH ('')
) [Incompletemonths]
From #Temp_IncompleteMonths t2
) [Main]

Insert INTO #Temp_StgvsPrdComparison (Population,Tabs,Checks,Results)
SELECT SubPopulation,'Patients','No of Patients',Count(1) FROM ABCR.AllPatientProfiles
GROUP BY SubPopulation
ORDER by SubPopulation

Insert INTO #Temp_StgvsPrdComparison (Population,Tabs,Checks,Results)
SELECT DISTINCT ' - ','PCPs','Check whether PCP page is populating', 'Warning' FROM (
select COALESCE(Cast(_PROV_UDF_01_ as varchar(max))
,Cast(_PROV_UDF_02_ as varchar(max))
,Cast(_PROV_UDF_03_ as varchar(max))
,Cast(_PROV_UDF_04_ as varchar(max))
,Cast(_PROV_UDF_05_ as varchar(max))
,Cast(_PROV_UDF_06_ as varchar(max))
,Cast(_PROV_UDF_07_ as varchar(max))
,Cast(_PROV_UDF_08_ as varchar(max))
,Cast(_PROV_UDF_09_ as varchar(max))
,Cast(_PROV_UDF_10_ as varchar(max))
,Cast(_PROV_UDF_11_ as varchar(max))
,Cast(_PROV_UDF_12_ as varchar(max))
,Cast(_PROV_UDF_13_ as varchar(max))
,Cast(_PROV_UDF_14_ as varchar(max))
,Cast(_PROV_UDF_15_ as varchar(max))
,Cast(_PROV_UDF_16_ as varchar(max))
,Cast(_PROV_UDF_17_ as varchar(max))
,Cast(_PROV_UDF_18_ as varchar(max))
,Cast(_PROV_UDF_19_ as varchar(max))
,Cast(_PROV_UDF_20_ as varchar(max))
) 'UDF' FROM Dbo.PROVIDER_UDF
) a
WHERE a.UDF IS NOT NULL

Insert INTO #Temp_StgvsPrdComparison (Population,Tabs,Checks,Results)
SELECT SubPopulation,'PCPs','No of PCPs',COUNT(DISTINCT prov_id) FROM ABCR.AllPatientProfiles
where prov_id is not NULL
GROUP BY SubPopulation
ORDER by SubPopulation

Insert INTO #Temp_StgvsPrdComparison (Population,Tabs,Checks,Results)
SELECT SubPopulation,'PCPs','PCPs with "Non-Domestic" Network Affiliation',COUNT(DISTINCT ap.prov_id) FROM ABCR.AllPatientProfiles ap
Inner JOIN PROVIDER p on ap.prov_id=p.PROV_ID
WHERE p.PROV_DOMESTIC='N'
and ap.prov_id IS NOT NULL
GROUP BY SubPopulation
ORDER by SubPopulation

Insert INTO #Temp_StgvsPrdComparison (Population,Tabs,Checks,Results)
SELECT SubPopulation,'PCPs','PCPs with "Unknown" Network Affiliation',COUNT(DISTINCT ap.prov_id) FROM ABCR.AllPatientProfiles ap
Inner JOIN PROVIDER p on ap.prov_id=p.PROV_ID
WHERE p.PROV_DOMESTIC<>'N' and p.PROV_DOMESTIC<>'Y'
and ap.prov_id IS NOT NULL
GROUP BY SubPopulation
ORDER by SubPopulation

Insert INTO #Temp_StgvsPrdComparison (Population,Tabs,Checks,Results)
SELECT SubPopulation,'PCPs','PCPs - Not Affiliated with practice',COUNT(DISTINCT ap.prov_id) FROM ABCR.AllPatientProfiles ap
Inner JOIN PROVIDER p on ap.prov_id=p.PROV_ID
WHERE p.ClinicName='Not Affiliated'
and ap.prov_id IS NOT NULL
GROUP BY SubPopulation
ORDER by SubPopulation

Insert INTO #Temp_StgvsPrdComparison (Population,Tabs,Checks,Results)
SELECT SubPopulation,'Practice','No of Practice',COUNT(DISTINCT p.ClinicName) FROM ABCR.AllPatientProfiles ap
Inner JOIN PROVIDER p on ap.prov_id=p.PROV_ID
Where p.ClinicName is NOT NULL
and ap.prov_id IS NOT NULL
GROUP BY SubPopulation
ORDER by SubPopulation

UPDATE tsp
Set tsp.DisplayName=pd.DisplayName
From #Temp_StgvsPrdComparison tsp
Inner Join ABCR.PopulationDates pd
On tsp.Population = pd.SubPopulation

DELETE ts
FROM #Temp_StgvsPrdComparison ts
Left Outer Join 
(SELECT DISTINCT _MI_USER_DIM_07_ From Mi.VW_CLAIM) pop
On ts.Population=pop._MI_USER_DIM_07_
WHERE pop._MI_USER_DIM_07_ IS NULL

SELECT * From #Temp_StgvsPrdComparison
ORDER by DisplayName,Tabs,Checks

BEGIN TRY

Drop TABLE #Temp_IncompleteMonths

END TRY
BEGIN CATCH
END CATCH

BEGIN TRY

Drop TABLE #Temp_StgvsPrdComparison

END TRY
BEGIN CATCH
END CATCH
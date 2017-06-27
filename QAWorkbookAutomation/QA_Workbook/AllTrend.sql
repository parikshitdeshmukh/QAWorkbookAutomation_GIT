SET NOCOUNT ON

BEGIN TRY
	DROP TABLE #Temp_Population_NoofMonths	
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY	
	DROP TABLE #Temp_ResultData	
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY
	DROP TABLE #Temp_FinalResult	
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY
DROP TABLE #Temp_ALLTrend
END TRY
BEGIN CATCH
END CATCH

CREATE TABLE #Temp_Population_NoofMonths(RNo Int Identity(1,1)
,[Population] Varchar(Max)
,[Display Name] Varchar(Max)
,LastValidDate Date
,LastClaimDate Date
,StartDate Date
,EndDate Date
,NoofMonths int
)

INSERT INTO #Temp_Population_NoofMonths(
[Population]
,[Display Name]
,LastValidDate
,LastClaimDate
,StartDate
,EndDate
,NoofMonths
)
SELECT SubPopulation 
,DisplayName
,LastValidDate
,LastClaimDate
,DATEADD(MONTH,-12,LastValidDate)
,DATEADD(MONTH,-1,LastValidDate)
,DATEDIFF(MONTH,LastValidDate,LastClaimDate)+13
FROM ABCR.PopulationDates

CREATE TABLE #Temp_ResultData(
[Population] Varchar(Max)
,[Display Name] Varchar(Max)
,Period  Varchar(Max)
,IsCompleted Char
,[IBNR Incurred Allowed] numeric(18,2)
,[IBNR Incurred Paid] numeric(18,2)
,[IBNR Amount Allowed - PaidClaims] numeric(18,2)
,[IBNR Amount Paid - PaidClaims] numeric(18,2)
)

CREATE TABLE #Temp_ALLTrend
([Population] Varchar(Max)
,DisplayName Varchar(Max)
,[Cost Basis] Varchar(10)
,Period Varchar(10)
,IsCompleted Char
,Trend Varchar(Max)
,[Trend Order] Int
,Data numeric(18,2)
)

DECLARE @NoofPopulations int,
@MaxPeriod Int,
@Year Varchar(4),
@NoofPeriods int,
@i int = 1,
@RNo int = 1 

SELECT @NoofPopulations=count(*) FROM #Temp_Population_NoofMonths

SELECT @NoofPeriods=Max(NoofMonths) From #Temp_Population_NoofMonths

WHILE (@RNo<=@NoofPopulations)
BEGIN

SELECT @MaxPeriod=DATEPART(MONTH,MAX(LastClaimDate)) From #Temp_Population_NoofMonths
WHERE RNo=@RNo

WHILE (@i<=@NoofPeriods)
BEGIN 
INSERT INTO #Temp_ResultData(
[Population]
,[Display Name]
,Period
,IsCompleted
)

SELECT [Population]
,[Display Name]
,DATEADD(MONTH, -@i+1, LastClaimDate)
,CASE WHEN DATEADD(MONTH, -@i+1, LastClaimDate)<LastValidDate THEN 'Y' ELSE 'N' END 
FROM #Temp_Population_NoofMonths
WHERE RNo=@RNo

SET @i=@i+1
END

SET @i=1
SET @RNo=@RNo+1
SET @i=1
END

DECLARE @StartDate date
DECLARE @EndDate date

SELECT @StartDate = MIN(Period),
@EndDate = MAX(Period)
FROM #Temp_ResultData

SELECT * INTO #Temp_FinalResult FROM 
(
SELECT tr.[Population]
,tr.[Display Name]
,tr.Period
,tr.IsCompleted
,ISNULL(CAST(PMPM.MedicalPMPM_Amt_Allowed AS numeric(18,2)),0) 'MedicalPMPM_Amt_Allowed'
,ISNULL(CAST(PMPM.RxPMPM_Amt_Allowed AS numeric(18,2)),0) 'RxPMPM_Amt_Allowed'
,ISNULL(CAST(PMPM.OverallPMPM_Amt_Allowed AS numeric(18,2)),0) 'OverallPMPM_Amt_Allowed'
,ISNULL(CAST(PMPM.MedicalPMPM_Amt_Paid AS numeric(18,2)),0) 'MedicalPMPM_Amt_Paid'
,ISNULL(CAST(PMPM.RxPMPM_Amt_Paid AS numeric(18,2)),0) 'RxPMPM_Amt_Paid'
,ISNULL(CAST(PMPM.OverallPMPM_Amt_Paid AS numeric(18,2)),0) 'OverallPMPM_Amt_Paid'
,ISNULL(CAST(Readmission.Readmission_Amt_Allowed AS numeric(18,2)),0) 'Readmission_Amt_Allowed'
,ISNULL(CAST(Readmission.Readmission_Amt_Paid AS numeric(18,2)),0) 'Readmission_Amt_Paid'
,ISNULL(Readmission.Readmission_Quality,0) 'Readmission_Quality'
,ISNULL(CAST(AvoidableAdmission.Avoidable_Admission_Amt_Allowed AS numeric(18,2)),0) 'Avoidable_Admission_Amt_Allowed'
,ISNULL(CAST(AvoidableAdmission.Avoidable_Admission_Amt_Paid AS numeric(18,2)),0) 'Avoidable_Admission_Amt_Paid'
,ISNULL(AvoidableAdmission.Avoidable_Admission_Quality,0) 'Avoidable_Admission_Quality'
,ISNULL(CAST(PMPM.OutpatientImaging_Amt_Allowed AS numeric(18,2)),0) 'OutpatientImaging_Amt_Allowed'
,ISNULL(CAST(PMPM.OutpatientImaging_Amt_Paid AS numeric(18,2)),0) 'OutpatientImaging_Amt_Paid'
,ISNULL(ROUND(CAST(PMPM.hop_MRI_casesP1PY AS numeric(18,2)),0),0) 'Hospiatl Outpatient MRIs per 1k'
,ISNULL(ROUND(CAST(PMPM.hop_CT_casesP1PY AS numeric(18,2)),0),0) 'Hospiatl Outpatient CTs per 1k'
,ISNULL(ROUND(CAST(PMPM.phy_MRI_utilsP1KPY AS numeric(18,2)),0),0) 'Office Based MRIs per 1k'
,ISNULL(ROUND(CAST(PMPM.phy_CT_utilsP1KPY AS numeric(18,2)),0),0) 'Office Based CTs per 1k'
,ISNULL(ROUND(CAST(PMPM.ED_Amt_Allowed AS numeric(18,2)),0),0) 'ED_Amt_Allowed'
,ISNULL(ROUND(CAST(PMPM.ED_Amt_Paid AS numeric(18,2)),0),0) 'ED_Amt_Paid'
,ISNULL(ROUND(CAST(PMPM.ED_Utilization AS numeric(18,2)),0),0) 'ED_Utilization'
,ISNULL(ROUND(CAST(PMPM.DrugGU_Amt_Allowed AS numeric(18,2)),0),0) 'DrugGU_Amt_Allowed'
,ISNULL(ROUND(CAST(PMPM.DrugGU_Amt_Paid AS numeric(18,2)),0),0) 'DrugGU_Amt_Paid'
,ISNULL(ROUND(GenericUtilization.Generic_Utilization,2),0) 'DrugGU'
,ISNULL(ROUND(CAST(CDM.CCHG_Allowed AS numeric(18,2)),0),0) 'CCHG_Allowed'
,ISNULL(ROUND(CAST(CDM.CCHG_Paid AS numeric(18,2)),0),0) 'CCHG_Paid'
,ISNULL(ROUND(CAST(CDMMembers.EnrolledMedicalMM / PMPM.MM_Units * 1000 AS numeric(18,2)),2),0) 'CCHG_Members'
--HCGSetting
,HCGSetting.[Inpatient Count] 'Inpatient Count'
,HCGSetting.[Outpatient Count] 'Outpatient Count'
,HCGSetting.[Professional Count] 'Professional Count'
,HCGSetting.[Ancillary Count] 'Ancillary Count'
,HCGSetting.[Drug Count] 'Drug Count'
--IBNR
,tr.[IBNR Incurred Allowed] 'IBNR Incurred Allowed'
,tr.[IBNR Incurred Paid] 'IBNR Incurred Paid'
,tr.[IBNR Amount Allowed - PaidClaims] 'IBNR Amount Allowed - PaidClaims'
,tr.[IBNR Amount Paid - PaidClaims] 'IBNR Amount Paid - PaidClaims'
FROM #Temp_ResultData tr
LEFT OUTER JOIN
(
--PMPM
SELECT
	Cost.[Population] 'Population',
	Cost.Period 'Period',
	ROUND(Cost.Medical_Amt_Allowed / MemberMonths.MM_UNITS, 2) 'MedicalPMPM_Amt_Allowed',
	ROUND(Cost.Rx_Amt_Allowed / MemberMonths.Rx_Units, 2) 'RxPMPM_Amt_Allowed',
	ROUND(Cost.Medical_Amt_Allowed / MemberMonths.MM_UNITS, 2) + ROUND(Cost.Rx_Amt_Allowed / MemberMonths.Rx_Units, 2) 'OverallPMPM_Amt_Allowed',
	ROUND(Cost.Medical_Amt_Paid / MemberMonths.MM_UNITS, 2) 'MedicalPMPM_Amt_Paid',
	ROUND(Cost.Rx_Amt_Paid / MemberMonths.Rx_Units, 2) 'RxPMPM_Amt_Paid',
	ROUND(Cost.Medical_Amt_Paid / MemberMonths.MM_UNITS, 2) + ROUND(Cost.Rx_Amt_Paid / MemberMonths.Rx_Units, 2) 'OverallPMPM_Amt_Paid',	
	ROUND(OutpatientImaging_Amt_Allowed,2) 'OutpatientImaging_Amt_Allowed',
	ROUND(OutpatientImaging_Amt_Paid,2) 'OutpatientImaging_Amt_Paid',
	ROUND(hop_MRI_casesP1PY / MemberMonths.MM_UNITS,2) 'hop_MRI_casesP1PY',
	ROUND(hop_CT_casesP1PY / MemberMonths.MM_UNITS,2) 'hop_CT_casesP1PY',
	ROUND(phy_MRI_utilsP1KPY / MemberMonths.MM_UNITS,2) 'phy_MRI_utilsP1KPY',
	ROUND(phy_CT_utilsP1KPY / MemberMonths.MM_UNITS,2) 'phy_CT_utilsP1KPY',
	ROUND(Cost.ED_Amt_Allowed, 2) 'ED_Amt_Allowed',
	ROUND(Cost.ED_Amt_Paid,2) 'ED_Amt_Paid',
	ROUND(Cost.ED_Utilization / MemberMonths.MM_UNITS,2) 'ED_Utilization',
	ROUND(Cost.Rx_Amt_Allowed,2) 'DrugGU_Amt_Allowed',
	ROUND(Cost.Rx_Amt_Paid,2) 'DrugGU_Amt_Paid',
	MemberMonths.MM_Units 'MM_Units'
FROM (SELECT
	c.subPopulation 'Population',
	CAST(c.Incurred_month_start_date AS date) 'Period',
	SUM(CASE
		WHEN c.HCG_MR_LINE NOT LIKE 'P81%' THEN ISNULL(c.Amt_allowed, 0)
		ELSE 0
	END) 'Medical_Amt_Allowed',
	SUM(CASE
		WHEN c.HCG_MR_LINE NOT LIKE 'P81%' THEN ISNULL(c.amt_paid, 0)
		ELSE 0
	END) 'Medical_Amt_Paid',
	SUM(CASE
		WHEN c.HCG_MR_LINE LIKE 'P81%' THEN ISNULL(c.Amt_allowed, 0)
		ELSE 0
	END) 'Rx_Amt_Allowed',
	SUM(CASE
		WHEN c.HCG_MR_LINE LIKE 'P81%' THEN ISNULL(c.amt_paid, 0)
		ELSE 0
	END) 'Rx_Amt_Paid',
	SUM(CASE 
	WHEN c.HCG_MR_LINE IN ('O14A', 'O14B', 'P59A', 'P59B') THEN ISNULL(c.Amt_allowed, 0) 
	ELSE 0 END) 'OutpatientImaging_Amt_Allowed',
	SUM(CASE 
	WHEN c.HCG_MR_LINE IN ('O14A', 'O14B', 'P59A', 'P59B') THEN ISNULL(c.amt_paid, 0) 
	ELSE 0 END) 'OutpatientImaging_Amt_Paid',
	SUM(12000 * (CASE
    WHEN HCG_MR_LINE = 'O14B' THEN ISNULL(c.utils,0)
    ELSE 0 END)) hop_MRI_casesP1PY,
	SUM(12000 * (CASE
    WHEN HCG_MR_LINE = 'O14A' THEN ISNULL(c.utils,0)
    ELSE 0 END)) hop_CT_casesP1PY,
    SUM(12000 * (CASE
    WHEN HCG_MR_LINE = 'P59B' THEN ISNULL(c.utils,0)
    ELSE 0 END)) phy_MRI_utilsP1KPY,
    SUM(12000 * (CASE
    WHEN HCG_MR_LINE = 'P59A' THEN ISNULL(c.utils,0)
    ELSE 0 END)) phy_CT_utilsP1KPY,
    SUM(CASE
    WHEN HCG_MR_LINE = 'O11a' THEN ISNULL(c.amt_allowed,0)
    ELSE 0 END) ED_Amt_Allowed,
    SUM(CASE
    WHEN HCG_MR_LINE = 'O11a' THEN ISNULL(c.amt_paid,0)
    ELSE 0 END) ED_Amt_Paid,
    SUM(12000 * (CASE
    WHEN HCG_MR_LINE = 'O11a' THEN ISNULL(c.utils,0)
    ELSE 0 END)) ED_Utilization
        
FROM ABCR.ClaimCostBy_M_MR c
WHERE c.INCURRED_MONTH_START_DATE >= @StartDate
AND c.INCURRED_MONTH_START_DATE <= @EndDate
GROUP BY	c.subPopulation,
			c.Incurred_month_start_date) Cost
INNER JOIN 
(
SELECT
	_MI_USER_DIM_07_ 'Population',
	MEMBER_MONTH_START_DATE 'Period',
	SUM(CASE
		WHEN PROD_TYPE = 'MEDICAL' THEN ISNULL(MM_UNITS, 0)
		ELSE 0
	END) MM_Units,
	SUM(CASE
		WHEN PROD_TYPE = 'RX' THEN ISNULL(RX_UNITS, 0)
		ELSE 0
	END) Rx_Units
FROM mI.VW_MEMBMTHS
WHERE MEMBER_MONTH_START_DATE >= @StartDate
AND MEMBER_MONTH_START_DATE <= @EndDate
GROUP BY	_MI_USER_DIM_07_,
			MEMBER_MONTH_START_DATE) MemberMonths
	ON Cost.[Population] = MemberMonths.[Population]
	AND Cost.Period = MemberMonths.Period
) PMPM
ON tr.[Population]=PMPM.[Population]
AND tr.Period=PMPM.Period
LEFT OUTER JOIN
(
--Readmisssion
SELECT r.SubPopulation 'Population'
,r.adm_INCURRED_MONTH_START_DATE 'Period'
,COUNT(1) 'Readmission_Quality'
,SUM(ISNULL(r.ReadmissionCostAllowed,0)) 'Readmission_Amt_Allowed'
,SUM(ISNULL(R.ReadmissionCost,0)) 'Readmission_Amt_Paid' 
From ABCR.Readmissions r
WHERE r.adm_INCURRED_MONTH_START_DATE >= @StartDate
AND r.adm_INCURRED_MONTH_START_DATE <=@EndDate
GROUP BY r.SubPopulation,r.adm_INCURRED_MONTH_START_DATE
) Readmission
ON tr.[Population]=Readmission.[Population]
AND tr.Period=Readmission.Period
LEFT OUTER JOIN
(
--Avoidable Admission
SELECT aa.subPopulation 'Population'
,aa.incurred_month_start_date 'Period'
,SUM(ISNULL(aa.AvoidableTotalAmountAllowed,0)) 'Avoidable_Admission_Amt_Allowed'
,SUM(ISNULL(aa.AvoidableTotalAmountPaid,0)) 'Avoidable_Admission_Amt_Paid'
,SUM(ISNULL(aa.AvoidableAdmissions,0)) 'Avoidable_Admission_Quality'
FROM ABCR.AvoidableAdmissionRollup aa
WHERE aa.incurred_month_start_date >=@StartDate
AND aa.incurred_month_start_date <= @EndDate
GROUP BY aa.subPopulation, aa.incurred_month_start_date
) AvoidableAdmission
ON tr.[Population]=AvoidableAdmission.[Population]
AND tr.Period=AvoidableAdmission.Period
LEFT OUTER JOIN
(
Select gu.subPopulation 'Population'
,gu.incurred_month_start_date 'Period'
,SUM(ISNULL(gu.genericUnits,0)) / SUM(ISNULL(gu.totalUnits,0)) 'Generic_Utilization'
From ABCR.DrugGenericUtilization_M_Class gu
WHERE gu.incurred_month_start_date >=@StartDate
AND gu.incurred_month_start_date <= @EndDate
GROUP BY gu.subPopulation, gu.incurred_month_start_date
) GenericUtilization
ON tr.[Population]=GenericUtilization.[Population]
AND tr.Period=GenericUtilization.Period
LEFT OUTER JOIN
(
select CCHG.subPopulation 'Population'
,CCHG.Incurred_month_start_date 'Period'
,SUM(ISNULL(CCHG.amt_allowed,0)) 'CCHG_Allowed'
,SUM(ISNULL(CCHG.amt_paid,0)) 'CCHG_Paid'
From ABCR.ClaimCostBy_M_CCHG CCHG
WHERE  CCHG.Incurred_month_start_date>=@StartDate
AND CCHG.Incurred_month_start_date <=@EndDate
GROUP BY CCHG.subPopulation,CCHG.Incurred_month_start_date
) CDM
ON tr.[Population]=CDM.[Population]
AND tr.Period=CDM.Period
LEFT OUTER JOIN
(
select CCHGMM.subPopulation 'Population'
,CCHGMM.month_start 'Period'
,SUM(ISNULL(CCHGMM.memberMonths,0)) 'EnrolledMedicalMM'
from ABCR.ChronicMemberMonths_Mo_CCHG CCHGMM
WHERE CCHGMM.month_start>=@StartDate
AND CCHGMM.month_start<=@EndDate
GROUP BY CCHGMM.subPopulation,CCHGMM.month_start
) CDMMembers
ON tr.[Population]=CDMMembers.[Population]
AND tr.Period=CDMMembers.Period
LEFT OUTER JOIN
(
SELECT _MI_USER_DIM_07_ 'Population'
,INCURRED_MONTH_START_DATE 'Period'
,SUM(CASE WHEN HCG_SETTING = '1-Inpatient' THEN 1 ELSE 0 END) 'Inpatient Count' 
,SUM(CASE WHEN HCG_SETTING = '2-Outpatient' THEN 1 ELSE 0 END) 'Outpatient Count' 
,SUM(CASE WHEN HCG_SETTING = '3-Professional' THEN 1 ELSE 0 END) 'Professional Count' 
,SUM(CASE WHEN HCG_SETTING = '4-Prescription Drug' THEN 1 ELSE 0 END) 'Drug Count' 
,SUM(CASE WHEN HCG_SETTING = '5-Ancillary' THEN 1 ELSE 0 END) 'Ancillary Count' 
From MI.VW_CLAIM
WHERE INCURRED_MONTH_START_DATE>=@StartDate
AND INCURRED_MONTH_START_DATE <=@EndDate
GROUP BY _MI_USER_DIM_07_,INCURRED_MONTH_START_DATE
) HCGSetting
ON tr.[Population]=HCGSetting.[Population]
AND tr.Period=HCGSetting.Period
--ORDER BY tr.[Population],tr.Period
) FinalResult

INSERT INTO #Temp_ALLTrend
(
[Population]
,DisplayName
,[Cost Basis]
,Period
,IsCompleted
,Trend
,[Trend Order]
,Data
)
-----------Allowed--------------------------
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Medical PMPM'
,1
,MedicalPMPM_Amt_Allowed
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Drug PMPM'
,2
,RxPMPM_Amt_Allowed
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted
,'Overall PMPM' 
,3
,OverallPMPM_Amt_Allowed
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Readmission Cost'
,4
,Readmission_Amt_Allowed
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Readmission Quality'
,5
,Readmission_Quality
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Avoidable Admission Cost'
,6
,Avoidable_Admission_Amt_Allowed
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Avoidable Admission Quality'
,7
,Avoidable_Admission_Quality
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Outpatient Imaging Cost'
,8
,OutpatientImaging_Amt_Allowed
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Hospiatl Outpatient MRIs per 1k'
,9
,[Hospiatl Outpatient MRIs per 1k]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Hospiatl Outpatient CTs per 1k'
,10
,[Hospiatl Outpatient CTs per 1k]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Office Based MRIs per 1k'
,11
,[Office Based MRIs per 1k]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Office Based CTs per 1k'
,12
,[Office Based CTs per 1k]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'ED Cost'
,13
,ED_Amt_Allowed
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted
,'ED Utilization' 
,14
,ED_Utilization
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Drug - Generic Utilization Cost'
,15
,DrugGU_Amt_Allowed
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Drug - Generic Utilization'
,16
,DrugGU
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Chronic Disease Management - Cost'
,17
,CCHG_Allowed
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Chronic Disease Management - Members'
,18
,CCHG_Members
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Inpatient Claim Count'
,19
,[Inpatient Count]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Outpatient Claim Count'
,20
,[Outpatient Count]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Professional Claim Count'
,21
,[Professional Count]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Ancillary Claim Count'
,22
,[Ancillary Count]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Allowed'
,Period
,IsCompleted 
,'Drug Claim Count'
,23
,[Drug Count]
FROM #Temp_FinalResult
UNION
-----------Paid--------------------------
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Medical PMPM'
,1
,MedicalPMPM_Amt_Paid
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Drug PMPM'
,2
,RxPMPM_Amt_Paid
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted
,'Overall PMPM' 
,3
,OverallPMPM_Amt_Paid
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Readmission Cost'
,4
,Readmission_Amt_Paid
FROM #Temp_FinalResult
UNION
SELECT [Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Readmission Quality'
,5
,Readmission_Quality
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Avoidable Admission Cost'
,6
,Avoidable_Admission_Amt_Paid
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Avoidable Admission Quality'
,7
,Avoidable_Admission_Quality
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Outpatient Imaging Cost'
,8
,OutpatientImaging_Amt_Paid
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Hospiatl Outpatient MRIs per 1k'
,9
,[Hospiatl Outpatient MRIs per 1k]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Hospiatl Outpatient CTs per 1k'
,10
,[Hospiatl Outpatient CTs per 1k]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Office Based MRIs per 1k'
,11
,[Office Based MRIs per 1k]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Office Based CTs per 1k'
,12
,[Office Based CTs per 1k]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'ED Cost'
,13
,ED_Amt_Paid
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted
,'ED Utilization' 
,14
,ED_Utilization
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Drug - Generic Utilization Cost'
,15
,DrugGU_Amt_Paid
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Drug - Generic Utilization'
,16
,DrugGU
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Chronic Disease Management - Cost'
,17
,CCHG_Paid
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Chronic Disease Management - Members'
,18
,CCHG_Members
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Inpatient Claim Count'
,19
,[Inpatient Count]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Outpatient Claim Count'
,20
,[Outpatient Count]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Professional Claim Count'
,21
,[Professional Count]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Ancillary Claim Count'
,22
,[Ancillary Count]
FROM #Temp_FinalResult
UNION
SELECT 
[Population]
,[Display Name] 
,'Paid'
,Period
,IsCompleted 
,'Drug Claim Count'
,23
,[Drug Count]
FROM #Temp_FinalResult

SELECT * From #Temp_ALLTrend
ORDER BY [Population],[Cost Basis],[Trend Order],Period


BEGIN TRY
	DROP TABLE #Temp_Population_NoofMonths	
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY	
	DROP TABLE #Temp_ResultData	
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY
	DROP TABLE #Temp_FinalResult	
END TRY
BEGIN CATCH
END CATCH

BEGIN TRY
DROP TABLE #Temp_ALLTrend
END TRY
BEGIN CATCH
END CATCH
INSERT INTO pais.mean_feature_vector 
SELECT 
 pais_uid,  
 AVG(AREA),
 AVG(PERIMETER),
 AVG(ECCENTRICITY),
 AVG(CIRCULARITY),
 AVG(MAJOR_AXIS),
 AVG(MINOR_AXIS),
 AVG(EXTENT_RATIO),
 AVG(MEAN_INTENSITY),
 AVG(MAX_INTENSITY ),
 AVG(MIN_INTENSITY ),
 AVG(STD_INTENSITY ),
 AVG(ENTROPY),
 AVG(ENERGY),
 AVG(SKEWNESS),
 AVG(KURTOSIS),
 AVG(MEAN_GRADIENT_MAGNITUDE),
 AVG(STD_GRADIENT_MAGNITUDE),
 AVG(ENTROPY_GRADIENT_MAGNITUDE),
 AVG(ENERGY_GRADIENT_MAGNITUDE),
 AVG(SKEWNESS_GRADIENT_MAGNITUDE),
 AVG(KURTOSIS_GRADIENT_MAGNITUDE),
 AVG(SUM_CANNY_PIXEL),
 AVG(MEAN_CANNY_PIXEL),
 AVG(CYTO_H_MeanIntensity),
 AVG(CYTO_H_MeanMedianDifferenceIntensity),
 AVG(CYTO_H_MaxIntensity),
 AVG(CYTO_H_MinIntensity),
 AVG(CYTO_H_StdIntensity),
 AVG(CYTO_H_Entropy),
 AVG(CYTO_H_Energy),
 AVG(CYTO_H_Skewness),
 AVG(CYTO_H_Kurtosis),
 AVG(CYTO_H_MeanGradMag),
 AVG(CYTO_H_StdGradMag),
 AVG(CYTO_H_EntropyGradMag),
 AVG(CYTO_H_EnergyGradMag),
 AVG(CYTO_H_SkewnessGradMag),
 AVG(CYTO_H_KurtosisGradMag),
 AVG(CYTO_H_SumCanny),
 AVG(CYTO_H_MeanCanny),
 AVG(CYTO_E_MeanIntensity),
 AVG(CYTO_E_MeanMedianDifferenceIntensity),
 AVG(CYTO_E_MaxIntensity),
 AVG(CYTO_E_MinIntensity),
 AVG(CYTO_E_StdIntensity),
 AVG(CYTO_E_Entropy),
 AVG(CYTO_E_Energy),
 AVG(CYTO_E_Skewness),
 AVG(CYTO_E_Kurtosis),
 AVG(CYTO_E_MeanGradMag),
 AVG(CYTO_E_StdGradMag),
 AVG(CYTO_E_EntropyGradMag),
 AVG(CYTO_E_EnergyGradMag),
 AVG(CYTO_E_SkewnessGradMag),
 AVG(CYTO_E_KurtosisGradMag),
 AVG(CYTO_E_SumCanny),
 AVG(CYTO_E_MeanCanny),
 AVG(CYTO_G_MeanIntensity),
 AVG(CYTO_G_MeanMedianDifferenceIntensity),
 AVG(CYTO_G_MaxIntensity),
 AVG(CYTO_G_MinIntensity),
 AVG(CYTO_G_StdIntensity),
 AVG(CYTO_G_Entropy),
 AVG(CYTO_G_Energy),
 AVG(CYTO_G_Skewness),
 AVG(CYTO_G_Kurtosis),
 AVG(CYTO_G_MeanGradMag),
 AVG(CYTO_G_StdGradMag),
 AVG(CYTO_G_EntropyGradMag),
 AVG(CYTO_G_EnergyGradMag),
 AVG(CYTO_G_SkewnessGradMag),
 AVG(CYTO_G_KurtosisGradMag),
 AVG(CYTO_G_SumCanny),
 AVG(CYTO_G_MeanCanny),
 STDDEV(AREA),
 STDDEV(PERIMETER),
 STDDEV(ECCENTRICITY),
 STDDEV(CIRCULARITY),
 STDDEV(MAJOR_AXIS),
 STDDEV(MINOR_AXIS),
 STDDEV(EXTENT_RATIO),
 STDDEV(MEAN_INTENSITY),
 STDDEV(MAX_INTENSITY ),
 STDDEV(MIN_INTENSITY ),
 STDDEV(STD_INTENSITY ),
 STDDEV(ENTROPY),
 STDDEV(ENERGY),
 STDDEV(SKEWNESS),
 STDDEV(KURTOSIS),
 STDDEV(MEAN_GRADIENT_MAGNITUDE),
 STDDEV(STD_GRADIENT_MAGNITUDE),
 STDDEV(ENTROPY_GRADIENT_MAGNITUDE),
 STDDEV(ENERGY_GRADIENT_MAGNITUDE),
 STDDEV(SKEWNESS_GRADIENT_MAGNITUDE),
 STDDEV(KURTOSIS_GRADIENT_MAGNITUDE),
 STDDEV(SUM_CANNY_PIXEL),
 STDDEV(MEAN_CANNY_PIXEL),
 STDDEV(CYTO_H_MeanIntensity),
 STDDEV(CYTO_H_MeanMedianDifferenceIntensity),
 STDDEV(CYTO_H_MaxIntensity),
 STDDEV(CYTO_H_MinIntensity),
 STDDEV(CYTO_H_StdIntensity),
 STDDEV(CYTO_H_Entropy),
 STDDEV(CYTO_H_Energy),
 STDDEV(CYTO_H_Skewness),
 STDDEV(CYTO_H_Kurtosis),
 STDDEV(CYTO_H_MeanGradMag),
 STDDEV(CYTO_H_StdGradMag),
 STDDEV(CYTO_H_EntropyGradMag),
 STDDEV(CYTO_H_EnergyGradMag),
 STDDEV(CYTO_H_SkewnessGradMag),
 STDDEV(CYTO_H_KurtosisGradMag),
 STDDEV(CYTO_H_SumCanny),
 STDDEV(CYTO_H_MeanCanny),
 STDDEV(CYTO_E_MeanIntensity),
 STDDEV(CYTO_E_MeanMedianDifferenceIntensity),
 STDDEV(CYTO_E_MaxIntensity),
 STDDEV(CYTO_E_MinIntensity),
 STDDEV(CYTO_E_StdIntensity),
 STDDEV(CYTO_E_Entropy),
 STDDEV(CYTO_E_Energy),
 STDDEV(CYTO_E_Skewness),
 STDDEV(CYTO_E_Kurtosis),
 STDDEV(CYTO_E_MeanGradMag),
 STDDEV(CYTO_E_StdGradMag),
 STDDEV(CYTO_E_EntropyGradMag),
 STDDEV(CYTO_E_EnergyGradMag),
 STDDEV(CYTO_E_SkewnessGradMag),
 STDDEV(CYTO_E_KurtosisGradMag),
 STDDEV(CYTO_E_SumCanny),
 STDDEV(CYTO_E_MeanCanny),
 STDDEV(CYTO_G_MeanIntensity),
 STDDEV(CYTO_G_MeanMedianDifferenceIntensity),
 STDDEV(CYTO_G_MaxIntensity),
 STDDEV(CYTO_G_MinIntensity),
 STDDEV(CYTO_G_StdIntensity),
 STDDEV(CYTO_G_Entropy),
 STDDEV(CYTO_G_Energy),
 STDDEV(CYTO_G_Skewness),
 STDDEV(CYTO_G_Kurtosis),
 STDDEV(CYTO_G_MeanGradMag),
 STDDEV(CYTO_G_StdGradMag),
 STDDEV(CYTO_G_EntropyGradMag),
 STDDEV(CYTO_G_EnergyGradMag),
 STDDEV(CYTO_G_SkewnessGradMag),
 STDDEV(CYTO_G_KurtosisGradMag),
 STDDEV(CYTO_G_SumCanny),
 STDDEV(CYTO_G_MeanCanny)
 FROM PAIS.CALCULATION_FLAT GROUP BY pais_uid; 

CALL pais.gen_all_histogram_area ;
CALL pais.gen_all_histogram_circularity ;
CALL pais.gen_all_histogram_eccentricity ;
CALL pais.gen_all_histogram_energy ;
CALL pais.gen_all_histogram_entropy ;
CALL pais.gen_all_histogram_max_intensity ;
CALL pais.gen_all_histogram_mean_intensity ;
CALL pais.gen_all_histogram_min_intensity ;
CALL pais.gen_all_histogram_perimeter ;
CALL pais.gen_all_histogram_std_intensity ;



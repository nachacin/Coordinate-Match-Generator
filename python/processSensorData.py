# Author: Nestor Chacin
# Processess coordinates from 2 sensors and produces a list of matched ID's and unmatched ID's
import pandas as pd
import numpy as np
from geopy import distance as geodist
import approx_distance as ap
import coordinate_globe_plotter as gplot
import distanceFoil as distF

# TODO: remove these file arrays before submission
JSON_PATHNAMES = ["data/easy1/input.json",
                  "data/easy2/input.json", 
				  "data/problem1/problem1.json", 
				  "data/problem2/problem2.json", 
				  "data/problem3/problem3.json"]
CSV_PATHNAMES = ["data/easy1/input.csv",
				 "data/easy2/input.csv", 
				 "data/problem1/problem1.csv", 
				 "data/problem2/problem2.csv", 
				 "data/problem3/problem3.csv"]
TEST_OUTPUT_PATHNAMES = ["test/easy1_test_output.txt", 
						 "test/easy2_test_output.txt", 
						 "test/problem1_test_output.txt", 
						 "test/problem2_test_output.txt", 
						 "test/problem3_test_output.txt"]
OUTPUT_PATHNAMES = ["output/easy1_output.txt", 
					"output/easy2_output.txt", 
					"output/problem1_output.txt", 
					"output/problem2_output.txt", 
					"output/problem3_output.txt"]

# Average radius of the earth in metres
R = 6371230
# Set value for maximum discrepancy between sensors
MAX_DISCREP = 100
# Get radian:degree
RAD_PER_DEG = np.pi/180
# Set comparison value
THRESHOLD = (MAX_DISCREP / (R * RAD_PER_DEG)) ** 2 
# Initialize an empty list for paired coordinates

# Geopy likes abs(latitude) to be less than 90 degrees. So this function provides that.
def get_act_coors(delta, alpha):
	if delta > 90:
		latitude = 180 - delta
		longitude = 180 + alpha
	elif delta < -90:
		latitude = -180 - delta
		longitude = 180 + alpha
	else:
		latitude = delta
		longitude = alpha
	return latitude, longitude

# Select files to analyze from user input
file_selection = int(input("Select files to process: "))
jdata = pd.read_json(JSON_PATHNAMES[file_selection - 1])
cdata = pd.read_csv(CSV_PATHNAMES[file_selection - 1])

# Shape DataFrame to have multiindex colums
jdata_grouped = jdata.set_index('Id')
jdata_grouped.columns = pd.MultiIndex.from_product([['Coordinates'], jdata_grouped.columns.tolist()])
jdata_grouped['Status'] = [-1 for i in range(len(jdata_grouped.index))]
cdata_grouped = cdata.set_index('Id')
cdata_grouped.columns = pd.MultiIndex.from_product([['Coordinates'], cdata_grouped.columns.tolist()])
cdata_grouped['Status'] = [-1 for i in range(len(cdata_grouped.index))]

# A way to perhaps have all data required in a single DataFrame
diff_array = jdata_grouped['Coordinates'].values - cdata_grouped['Coordinates'].values[:, None]

# Generates test output files by calculating distance between every coordinate
# with the distance function in the Geopy library

paired_test = []
unpaired_test = []

for j in range(len(jdata_grouped.index)):
	for c in range(len(cdata_grouped.index)):
		jlat, jlon = get_act_coors(jdata_grouped.Coordinates.Latitude.iloc[j], jdata_grouped.Coordinates.Longitude.iloc[j])
		clat, clon = get_act_coors(cdata_grouped.Coordinates.Latitude.iloc[c], cdata_grouped.Coordinates.Longitude.iloc[c])
		distance = geodist.distance((jlat, jlon), (clat, clon)).meters
		if distance < 100:
			paired_test.append((cdata_grouped.index[c], jdata_grouped.index[j])) # DataFrame index attribute is set to use Id
			cdata_grouped.loc[cdata_grouped.index[c], 'Status'] = 0
			jdata_grouped.loc[jdata_grouped.index[j], 'Status'] = 0

for i in range(len(jdata_grouped.index)):
	if jdata_grouped.Status.iloc[i] == -1:
		unpaired_test.append((-1, jdata_grouped.index[i]))

for i in range(len(cdata_grouped.index)):
	if cdata_grouped.Status.iloc[i] == -1:
		unpaired_test.append((cdata_grouped.index[i], -1))

overall_list_test = paired_test + unpaired_test

with open(TEST_OUTPUT_PATHNAMES[file_selection - 1], 'w+') as f:
	for elements in overall_list_test:
		f.write(str(elements[0]) + ":" + str(elements[1]) + "\n")
	print("Test output generated successfully in " + TEST_OUTPUT_PATHNAMES[file_selection - 1])
f.close()

# Generates the same output as Geopy's distance function using a method with
# small angle approximation

jdata['Status'] = pd.Series([-1 for i in range(len(jdata.index))])
cdata['Status'] = pd.Series([-1 for i in range(len(cdata.index))])

paired = []
unpaired = []

for j in range(len(jdata.index)):
	for c in range(len(cdata.index)):
		measure = (jdata.Latitude[j] - cdata.Latitude[c]) ** 2 + ((jdata.Longitude[j] - cdata.Longitude[c]) * np.cos(np.radians(jdata.Latitude[j]))) ** 2
		if measure < THRESHOLD:
			paired.append((cdata.Id[c], jdata.Id[j]))
			cdata.loc[c, 'Status'] += 1
			jdata.loc[j, 'Status'] += 1
			break

for i in range(len(jdata.index)):
	if jdata.Status[i] == -1:
		unpaired.append((-1, jdata.Id[i]))
	elif jdata.Status[i] > 0:
		print("Error, more than one match found for coordinates in JSON file:")
		print(jdata.loc[[i]])

for i in range(len(cdata.index)):
	if cdata.Status[i] == -1:
		unpaired.append((cdata.Id[i], -1))
	elif cdata.Status[i] > 0:
		print("Error, more than one match found for coordinates in CSV file:")
		print(cdata.loc[[i]])

overall_list = paired + unpaired

with open(OUTPUT_PATHNAMES[file_selection - 1], 'w+') as f:
	for elements in overall_list:
		f.write(str(elements[0]) + ":" + str(elements[1]) + "\n")
	print("Output generated successfully in" + OUTPUT_PATHNAMES[file_selection - 1])
f.close()
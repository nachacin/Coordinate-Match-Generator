import pandas as pd
import numpy as np
from geopy import distance as geodist
import approx_distance as ap

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


def distances_geo(jdata, cdata):

    dist_array_geo = np.zeros((len(jdata.index),len(cdata.index)), dtype = np.float64)

    for j in range(len(jdata.index)):
        for c in range(len(cdata.index)):
            jlat, jlon = get_act_coors(jdata.Latitude[j], jdata.Longitude[j])
            clat, clon = get_act_coors(cdata.Latitude[c], cdata.Longitude[c])
            dist_array_geo[j][c] = geodist.distance((jlat, jlon), (clat, clon)).meters

    dist_df_geo = pd.DataFrame(dist_array_geo, index = pd.Series(jdata.Id.values, name = 'JSON IDs '), columns = pd.Series(cdata.Id.values, name = 'CSV  IDs ->'))
    return dist_array_geo

def distances_haver(jdata, cdata):
    dist_array_haver = np.zeros((len(jdata.index),len(cdata.index)), dtype = np.float64)

    for j in range(len(jdata.index)):
        for c in range(len(cdata.index)):
            dist_array_haver[j][c] = ap.great_circle_distance(jdata.Latitude[j], jdata.Longitude[j], cdata.Latitude[c], cdata.Longitude[c])

    dist_df_haver = pd.DataFrame(dist_array_haver, index = pd.Series(jdata.Id.values, name = 'JSON IDs '), columns = pd.Series(cdata.Id.values, name = 'CSV  IDs ->'))
    return dist_df_haver

def distances_small_angle(jdata, cdata):
    dist_array_approx = np.zeros((len(jdata.index),len(cdata.index)), dtype = np.float64)

    for j in range(len(jdata.index)):
        for c in range(len(cdata.index)):
            dist_array_approx[j][c] = ap.dist_small_ang(jdata.Latitude[j], jdata.Longitude[j], cdata.Latitude[c], cdata.Longitude[c])

    dist_df_approx = pd.DataFrame(dist_array_approx, index = pd.Series(jdata.Id.values, name = 'JSON IDs '), columns = pd.Series(cdata.Id.values, name = 'CSV  IDs ->'))
    return dist_df_approx
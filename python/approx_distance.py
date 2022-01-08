import numpy as np
# Constant for Earth radius in metres
R = 6371230

def dist_small_ang(lat1, lon1, lat2, lon2):
    delta1 = np.radians(lat1)
    delta2 = np.radians(lat2)
    phi1 = np.radians(lon1)
    phi2 = np.radians(lon2)
    delta_diff = delta2 - delta1
    phi_diff = phi2 - phi1
    c = np.sqrt((phi_diff * np.cos(delta2)) ** 2 + delta_diff ** 2)
    return R * c

def great_circle_distance(lat1, lon1, lat2, lon2):
    
    delta1 = np.radians(lat1)
    delta2 = np.radians(lat2)
    phi1 = np.radians(lon1)
    phi2 = np.radians(lon2)
    # Calculate half of squared chord length
    a = np.sin((delta2 - delta1) / 2) ** 2 + np.cos(delta1) * np.cos(delta2) * np.sin((phi2 - phi1) / 2) ** 2
    # Calculate central angle
    c = 2 * np.arctan2( np.sqrt(a), np.sqrt(1 - a))
    return R * c

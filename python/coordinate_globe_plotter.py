import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import mpl_toolkits.mplot3d.axes3d as axes3d

def plot_coors(coor_df):
    theta, phi = np.linspace(0, 2 * np.pi, 50), np.linspace(0, np.pi, 50)
    THETA, PHI = np.meshgrid(theta, phi)
    R = 0.5
    X = R * np.sin(PHI) * np.cos(THETA)
    Y = R * np.sin(PHI) * np.sin(THETA)
    Z = R * np.cos(PHI)
    fig = plt.figure()
    ax = fig.add_subplot(1,1,1, projection='3d')
    plot_surf = ax.plot_surface(
        X, Y, Z, rstride=1, cstride=1, cmap=plt.get_cmap('jet'),
        linewidth=0, alpha=0.3)

    toDeg = np.pi / 180

    # Plot all points in coordinate set

    xx = 0.5 * np.cos(coor_df.Latitude * toDeg) * np.cos(coor_df.Longitude * toDeg)
    yy = 0.5 * np.cos(coor_df.Latitude * toDeg) * np.sin(coor_df.Longitude * toDeg)
    zz = 0.5 * np.sin(coor_df.Latitude * toDeg)
    plot_points = ax.scatter(xx, yy, zz, color='k', s=20)

    plt.show()
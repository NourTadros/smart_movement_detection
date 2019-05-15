from scipy.signal import butter
from sklearn import svm
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report,accuracy_score
from sklearn.model_selection import train_test_split
import numpy as np
from mpl_toolkits import mplot3d
from sklearn.naive_bayes import GaussianNB, BernoulliNB
from sklearn.neighbors import KNeighborsClassifier
from sklearn.neural_network import MLPClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
import glob
from sklearn.decomposition import PCA
from sklearn.feature_selection import SelectFromModel
import plotly.plotly as py
import plotly.graph_objs as go
import plotly.figure_factory as ff
import scipy
from scipy import stats
import matplotlib.pyplot as plt
import math
from scipy import signal
from scipy.signal import butter, lfilter, freqz

#from hmmlearn import hmm
import time
import pyrebase

from firebase import firebase
from mpl_toolkits import mplot3d


start=time.time()



#firebas
config = {
  "apiKey": "apiKey",
  "authDomain": "accelerometerreadings-1eeea.firebaseapp.com ",
  "databaseURL": "https://accelerometerreadings-1eeea.firebaseio.com/",
  "storageBucket": "gs://accelerometerreadings-1eeea.appspot.com"

}

firebase = pyrebase.initialize_app(config)
db = firebase.database()



all_readings = db.child("accelerometerReadings").get()

acc_x = []
acc_y=[]
acc_z=[]
timestamp=[]
accelerometerPrediction=[]
for readings in all_readings.each():
    accelerometerPrediction.append([readings.val()["acc_x"], readings.val()["acc_y"], readings.val()["acc_z"]])

    timestamp.append(readings.val()['timestamp'])

accelerometerPrediction= np.array(accelerometerPrediction)



dataSort=pd.read_csv("FallDataset.csv") #61%

COLUMN_NAMES=dataSort.iloc[:,0:3].values

LABELS=dataSort.iloc[:,3].values

dataClf=pd.read_csv("FallDataset.csv") #61%

COLUMNS=dataClf.iloc[:,0:3].values

TYPES=dataClf.iloc[:,3].values





plt.figure(figsize=(10, 12))
#
plt.plot(dataSort[dataSort['label']=='FOL'][['acc_x']],color='red', label="Fall on hands")
plt.title("Fall on hands")
plt.xlabel('Timestep')
plt.ylabel('X Acceleration')
plt.legend()
plt.show()

plt.plot(dataSort[dataSort['label']=='FKL'][['acc_x']], color='blue', label="Fall forward on Knees")
plt.title("Fall forward on Knees")
plt.xlabel('Timestep')
plt.ylabel('X Acceleration')
plt.legend()
plt.show()

plt.plot(dataSort[dataSort['label']=='BSC'][['acc_x']], color='gray', label="Fall Backward")
plt.title("Fall Backward")
plt.xlabel('Timestep')
plt.ylabel('X Acceleration')
plt.legend()
plt.show()

plt.plot(dataSort[dataSort['label']=='SDL'][['acc_z']],color='green',label="Fall Sideward")
plt.title("Fall Sideward")
plt.xlabel('Timestep')
plt.ylabel('Z Acceleration')
plt.legend()
plt.show()

plt.plot(dataSort[dataSort['label']=='STD'][['acc_y']],color='orange',label='Standing')
plt.title("Standing")
plt.xlabel('Timestep')
plt.ylabel('Y Acceleration')
plt.legend()
plt.show()

plt.plot(dataSort[dataSort['label']=='LYI'][['acc_z']],color='black',label="Lying on floor")
plt.title("Lying on floor")
plt.xlabel('Timestep')
plt.ylabel('Z Acceleration')
plt.legend()
plt.show()




X_train, X_test, y_train, y_test = train_test_split(COLUMNS, TYPES, test_size=0.3, random_state=42)



clf=LogisticRegression( random_state=0,solver='lbfgs',multi_class='auto')

clf.fit(X_train, y_train)

accelerometerPrediction=accelerometerPrediction.astype(float)
FallingType = clf.predict(accelerometerPrediction)
print (FallingType)

y_pred=clf.predict(X_test)


# # #
print("Accuracy of training: " ,accuracy_score(y_test, y_pred)*100)
#
end=time.time()
print("time:", end-start)

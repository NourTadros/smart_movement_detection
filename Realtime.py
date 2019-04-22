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

from scipy import signal
from scipy.signal import butter, lfilter, freqz

#from hmmlearn import hmm
import time
import pyrebase

from firebase import firebase


start=time.time()

# firebase = firebase.FirebaseApplication('https://accelerometerreadings-1eeea.firebaseio.com/', None)
# result = firebase.get('/accelerometerReadings/-LcWyAqRFBg8ngR1X201', None)


# print(result)

#firebas
config = {
  "apiKey": "apiKey",
  "authDomain": "accelerometerreadings-1eeea.firebaseapp.com ",
  "databaseURL": "https://accelerometerreadings-1eeea.firebaseio.com/",
  "storageBucket": "gs://accelerometerreadings-1eeea.appspot.com"

}

firebase = pyrebase.initialize_app(config)
db = firebase.database()

# readings = db.child("accelerometerReadings").get()
# print(readings.val())

all_readings = db.child("accelerometerReadings").get()

acc_x = []
acc_y=[]
acc_z=[]
timestamp=[]

for readings in all_readings.each():
    #print(readings.key())
    acc_x.append(readings.val()["acc_x"])
    acc_y.append(readings.val()["acc_y"])
    acc_z.append(readings.val()["acc_z"])
    timestamp.append(readings.val()['timestamp'])


print("accelerometer X: ",acc_x)
print("accelerometer Y: ",acc_y)
print("accelerometer Z: ",acc_z)
print("Timestamp: ",timestamp)


dataSort=pd.read_csv("FALL_Realtime_TS_Sort.csv") #61%

COLUMN_NAMES=dataSort.iloc[:,0:4].values

LABELS=dataSort.iloc[:,4].values

dataClf=pd.read_csv("FALL_Realtime_TS.csv") #61%

COLUMNS=dataClf.iloc[:,0:4].values

TYPES=dataClf.iloc[:,4].values



SEGMENT_TIME_SIZE = 180
TIME_STEP = 100
# print(data)
data_convoluted = []
labels = []
x= []
y=[]
z=[]

plt.figure(figsize=(10, 12))

plt.plot(dataSort[dataSort['label']=='FOL'][['acc_x']], color='red', label="Fall on hands")
plt.title("Fall on hands")
plt.xlabel('Timestep')
plt.ylabel('X acceleration')
plt.legend()
plt.show()
plt.plot(dataSort[dataSort['label']=='FKL'][['acc_x']], color='blue', label="Fall forward on Knees")
plt.title("Fall forward on Knees")
plt.xlabel('Timestep')
plt.ylabel('X acceleration')
plt.legend()
plt.show()
plt.plot(dataSort[dataSort['label']=='BSC'][['acc_x']], color='gray', label="Fall Backward")
plt.title("Fall Backward")
plt.xlabel('Timestep')
plt.ylabel('X acceleration')
plt.legend()
plt.show()
plt.plot(dataSort[dataSort['label']=='SDL'][['acc_x']],color='green',label="Fall Sideward")
plt.title("Fall Sideward")
plt.xlabel('Timestep')
plt.ylabel('X acceleration')
plt.legend()
plt.show()
plt.plot(dataSort[dataSort['label']=='STD'][['acc_x']],color='orange',label='Standing')
plt.title("Standing")
plt.xlabel('Timestep')
plt.ylabel('X acceleration')
plt.legend()
plt.show()
plt.plot(dataSort[dataSort['label']=='LYI'][['acc_x']],color='black',label="Lying on floor")
# plt.plot(diffz,color='green',label="What of z was removed")
plt.title("Lying on floor")
plt.xlabel('Timestep')
plt.ylabel('X acceleration')
plt.legend()
plt.show()

for i in range(0, len(dataClf) - SEGMENT_TIME_SIZE, TIME_STEP):
    x = dataClf['acc_x'].values[i: i + SEGMENT_TIME_SIZE]
    y = dataClf['acc_y'].values[i: i + SEGMENT_TIME_SIZE]
    z = dataClf['acc_z'].values[i: i + SEGMENT_TIME_SIZE]
    # print(x)
    data_convoluted.append([x, y, z])

    #Label for a data window is the label that appears most commonly
    label = stats.mode(dataClf['label'][i: i + SEGMENT_TIME_SIZE])[0][0]
    labels.append(label)

#Convert to numpy
data_convoluted = np.asarray(data_convoluted, dtype=np.float32).transpose(0, 2, 1)

labels = np.asarray(pd.get_dummies(labels), dtype=np.float32)
data=[]
# print(data_convoluted.shape)
for i in range(len(data_convoluted)):
   data.append(data_convoluted[i].flatten())

data = np.array(data)
# print(len(data_convoluted))
# print(len(data.shape))

# print("Convoluted data shape: ", data.shape)

# print("Labels shape:", len(labels))


# print (len(x))
# print (len(y))
# print (len(z))



X_train, X_test, y_train, y_test = train_test_split(data, labels, test_size=0.3, random_state=42)


clf=RandomForestClassifier(n_estimators=100, random_state=1)
clf.fit(X_train, y_train)
y_pred = clf.predict(X_test)
# print (y_pred)
# # #
print("Accuracy: " + str(accuracy_score(y_test, y_pred)*100))
#
end=time.time()
print("time:", end-start)

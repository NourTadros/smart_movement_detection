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
import codecs, json
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
import smtplib
import pyrebase


from firebase import firebase
from mpl_toolkits import mplot3d

#3ashan n3raf bedyat el runtime
start=time.time()



#firebas
config = {
  "apiKey": "apiKey",
  "authDomain": "accelerometerreadings-1eeea.firebaseapp.com ",
  "databaseURL": "https://accelerometerreadings-1eeea.firebaseio.com/",
  "storageBucket": "gs://accelerometerreadings-1eeea.appspot.com"

}
#pyrebase used to connect python to firebase
#pyrebase da firebase SDK
firebase = pyrebase.initialize_app(config)
db = firebase.database()



#bn7ot f all readings el children bto3 accelerometer readings ely homa el data
all_readings = db.child("accelerometerReadings").get()

acc_x = []
acc_y=[]
acc_z=[]
timestamp=[]
accelerometerPrediction=[]

#looping on each reading in all readings of accelerometer readings children
for readings in all_readings.each():
    #bnakhod variable bn7ot feh el c w el y w el z
    accelerometerPrediction.append([readings.val()["acc_x"], readings.val()["acc_y"], readings.val()["acc_z"]])
#timestamp lwhdo
    timestamp.append(readings.val()['timestamp'])

#array of accelerometer predictions by numpy
accelerometerPrediction= np.array(accelerometerPrediction)


#sorted data for figures
dataSort=pd.read_csv("FallDataset.csv")

#looping on data columns
COLUMN_NAMES=dataSort.iloc[:,0:3].values

#looping on label
LABELS=dataSort.iloc[:,3].values

#our dataset
dataClf=pd.read_csv("FallDataset.csv")

#looping on columns
COLUMNS=dataClf.iloc[:,0:3].values

#looping on labels
TYPES=dataClf.iloc[:,3].values




#figurePlotting

plt.figure(figsize=(10, 12))
#
plt.plot(dataSort[dataSort['label']=='FOL'][['acc_x']],color='red', label="Fall on hands")
plt.title("Fall on hands")
plt.xlabel('Timestep')
plt.ylabel('X Acceleration')
plt.legend()
# plt.show()

plt.plot(dataSort[dataSort['label']=='FKL'][['acc_x']], color='blue', label="Fall forward on Knees")
plt.title("Fall forward on Knees")
plt.xlabel('Timestep')
plt.ylabel('X Acceleration')
plt.legend()
# plt.show()

plt.plot(dataSort[dataSort['label']=='BSC'][['acc_x']], color='gray', label="Fall Backward")
plt.title("Fall Backward")
plt.xlabel('Timestep')
plt.ylabel('X Acceleration')
plt.legend()
# plt.show()

plt.plot(dataSort[dataSort['label']=='SDL'][['acc_y']],color='green',label="Fall Sideward")
plt.title("Fall Sideward")
plt.xlabel('Timestep')
plt.ylabel('Y Acceleration')
plt.legend()
# plt.show()

plt.plot(dataSort[dataSort['label']=='STD'][['acc_y']],color='orange',label='Standing')
plt.title("Standing")
plt.xlabel('Timestep')
plt.ylabel('Y Acceleration')
plt.legend()
# plt.show()

plt.plot(dataSort[dataSort['label']=='LYI'][['acc_z']],color='black',label="Lying on floor")
plt.title("Lying on floor")
plt.xlabel('Timestep')
plt.ylabel('Z Acceleration')
plt.legend()
# plt.show()



#train test split byakhod el columns bt3tna w el labels
# #w el test size b 30% w
# #el random state 3ashan to guarantee en each time byaakhod random for train and test
X_train, X_test, y_train, y_test = train_test_split(COLUMNS, TYPES, test_size=0.3, random_state=42)


#classifier logistic regression
#random state
#solver
#multiclass
# clf=LogisticRegression( random_state=None,solver='saga',multi_class='auto')#default none, saga faster for large dataset, #81
clf=SVC(gamma='scale', decision_function_shape='ovo')#95
# clf=MLPClassifier(solver='lbfgs', alpha=1e-5,hidden_layer_sizes=(5, 2), random_state=1)#75
#bn3mel fitting ll data
clf.fit(X_train, y_train)

#el array bt3 el predictions 3amalnalo predict el gybnha mn l firebase
accelerometerPrediction=accelerometerPrediction.astype(float)
FallingType = clf.predict(accelerometerPrediction)
print (FallingType)

#predictions of our dataset
y_pred=clf.predict(X_test)

# prediction=db.push('/predictions',{'prediction':np.array(FallingType)})
# print(prediction)
# data = { 'predictions': FallingType}
#
# results = db.child("test").get(all_readings['id'])
# print (results.val())
# result = firebase.post("/predictions", data)
# # #

content='Your patient fell'
mail=smtplib.SMTP('smtp.gmail.com',587)
mail.ehlo()
mail.starttls()
mail.login('nour.e.tadros98@gmail.com','kiikiikaty7')
mail.sendmail('fromemail','receiver',content)
mail.close()
#accuracy of our dataset
print("Accuracy of training: " ,accuracy_score(y_test, y_pred)*100)
#
end=time.time() #the end of runtime
print("time:", end-start)

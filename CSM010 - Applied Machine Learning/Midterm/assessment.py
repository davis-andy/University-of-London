# import the appropriate libraries
import pandas as pd
from sklearn.model_selection import KFold
from sklearn.model_selection import cross_val_score
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from numpy import around, set_printoptions


def main():
    # Load the data
    filename = './data/exoplanets_2018.csv'
    data = pd.read_csv(filename)

    # Extract X and Y
    array = data.values
    X = array[:,0:data.shape[1] - 1]
    Y = array[:,data.shape[1] - 1]

    set_printoptions(precision=2)
    
    # Setup k-fold
    seed = 7
    kfold = KFold(n_splits=10, random_state=seed, shuffle=True)

    # Setup Logistic Regression + Cross validation
    lrmodel = LogisticRegression(solver='liblinear', max_iter=2000, C=100)
    lrresults = cross_val_score(lrmodel, X, Y, cv=kfold)
  
    # Setup Decision Tree Classifier + Cross validation
    dtmodel = DecisionTreeClassifier()
    dtresults = cross_val_score(dtmodel, X, Y, cv=kfold)

    # Setup Random Forest Classifier + Cross validation
    rfmodel = RandomForestClassifier(n_estimators=100, criterion='gini')
    rfresults = cross_val_score(rfmodel, X, Y, cv=kfold)
    
    # Return the average cross valiation score for each model
    # as an array storing the values in the following format:
    # [0.73, 0.78, 0.80]
    
    averages = [lrresults.mean(), dtresults.mean(), rfresults.mean()]
    return around(averages, decimals=2) # set_printoptions doesn't seem to work, so I am rounding it

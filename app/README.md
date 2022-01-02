# Title: Property Manger Application Android

## Description:
A simple Android application for managing a property portfolio

## Functionality
## 1) Login and Signup View:
App allows users to create an account using either Email and password or using an existing Google
Account. These functions are supported using Firebase Auth API and Google Auth API. 

## 2) Property List View
App makes calls to database and storage using Firebase Auth, Firebase DB, Firebase Storage 
dependencies to retrieve a list of properties for a given user and displays them using a
RecyclerView.

###Functions: 
Swipe Delete - using a swipe delete callback
Swipe Edit - using a swipe edit callback - Triggers edit location view.
Floating Add Button - Triggers Add Property View
onPropertyClick - Edit selected property. Launches Edit Property View
Add to Favourites - Each Property Card has a star button to add to favourites list. Can be toggled
on/off
Toolbar Toggle Favourite View - This will call method to get users favourite properties and display
in Recycler View

## 3) Add Property/Edit Property View
This view will allow users to input details about the Property and add/update the property depending
on if it is new or existing.  Details are stored in Firebase RTDB.
User can add image which is uploaded to Cloud Storage (Firebase). Location can be set using Google 
Maps API and Google Location.  

### Functions
Save Property - Add/Update Property details and update DB and Cloud Storage for Images
Delete Property - Only visible in Toolbar if editing existing property.
Add/Update Image - Launches devices image picker can select new image from device storage.
onMapClick - Launches Edit Location View.

## 4) Edit Location/View
Launches Google Maps widget in view and displays marker for property. This marker can be dragged 
and dropped new location saved on property update. Bug here - On launcg currently displays devices l
ocation using live location but property location save correctly to DB on Edit
as can be seen in Maps View.

## 5) Maps View
Launches a map which displays Markers for each property saved. These locations are fetched from DB 
using coroutines and converted to correct format.

### Functions
onMarkerClick - User can select a marker and will see a card summary of Property including image, 
title and description.

## UX/DX Approach Adopted

### UX
App easily navigable for Users through the use of Toolbar Menus, Floating Action Buttons and 
Toggle Buttons.

Menu Main - Options to Add (Also by visible Floating action Button), View Map, View Favourites (Also supported by visible toggle), Logout.
Property View Menu - Cancel, Save for new Properties. Cancel, Save, Delete or existing.

### DX

### Application Architecture
Model View Presenter approach with Toolbar controlled navigation. Fragments not implemented so 
navigation controlled by startActivity() etc coupled with Intents.

## Git Approach
Two main branches Master (Release) and Develop Branch. For each feature suite created new branch on
Develop for feature once satisfied created pull request on Github and merged. Once feature merged 
and tested and fixed on Develop merged to Master using a pull request on Github. This commit on 
Master branch was Tagged and used to create a (pre-) Release on Github.

## Personal Statement
I hereby certify that this project is my own work developed using lecture and lab materials from 
WIT HDip in Computer Science Mobile Application Development module taughed by David Drohan and David 
Hearne. 

##References
In addtion, I refered to Android Developer documentation and Firebase Documentation.

https://firebase.google.com/docs?gclid=Cj0KCQiAt8WOBhDbARIsANQLp96VxBF43c8GbnhWRRw-rtjpw3QW86bdHPbtec1T5PG3UjaEhG0DnQUaAq5EEALw_wcB&gclsrc=aw.ds
https://developer.android.com/docs

### Name: Gary Houston
### Student ID: 20091416






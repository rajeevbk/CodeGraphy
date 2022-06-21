# CodeGraphy
A handwriting based android python IDE

First stage of my research study was to identify a suitable HWR engine for source code recognition 

https://github.com/rajeevbk/Handwritten-SourceCode-Recognition-For-Python


This is the second phase where I build a sample mobile IDE that uses handwritting as the main input source.

Documentation for Digital INK: https://developers.google.com/ml-kit/vision/digital-ink-recognition/android

This code was derived from the sample here: https://github.com/googlesamples/mlkit/tree/master/android/digitalink



## TODO:

* Currently, the syntax highlighted "codeview" is not editable. Figure out how to create an editable python-syntax-highlighted codeview
* Give the user the ability to insert common programming constructs using handwritten variables (e.g create a for loop around a variable)
* Cleaning up the UI
* The inserted code's indentation is handled by merely inserting a space before the text. Right now it supports only 3 levels, need a better way to do this.

from django.shortcuts import render
from django.http import HttpResponse
from .models import usertable2
from .models import booktable2
from django.template import loader
from django.conf import settings

# Create views here.
#This one renders an index html file when the index is requested by the user
#In this case, the index is the 'Admin' (As seen in libraryproject2/urls.py)
def index(request):
    return render(request, 'index.html')




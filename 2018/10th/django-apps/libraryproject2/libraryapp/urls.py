from django.urls import path
from . import views
from django.contrib import admin

#Directs the person who accesses the "server" to the admin site url when no path is defined; the default.
#In this case, the 'Admin site' is repurposed as the main site
urlpatterns = [
    path('', admin.site.urls),
]

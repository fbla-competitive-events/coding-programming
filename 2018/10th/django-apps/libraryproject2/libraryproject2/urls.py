from django.contrib import admin
from django.urls import include, path



#urls that are called when user requests them through the main site:
#Empty path, libraryapp/, and admin/ all return the admin.site.urls pages from Django
urlpatterns = [
    path('user_guide/', include),
    path('', include('libraryapp.urls')),
    path('libraryapp/', include('libraryapp.urls')),
    path('admin/', admin.site.urls),
]


#Changes major header names of the website
admin.site.site_header = ("HelloWorld High Library Management System")
admin.site.site_title = ("HWS Library")


#Imports Admin fom Django
from django.contrib import admin

#Imports the 2 tables from the Library Database (All Data is populated fictionally)
from .models import usertable2
from .models import booktable2

#REGISTER MODELS

#Action function that sets all fields dependent on it being checked out to default; clears information from fields when Check In Books Is selected
def checkin_books(modeladmin, request, queryset):
    queryset.update(in_library=True, user_id="INLIBR", book_due_date=None, checked_out_date=None, days_until_due=None)
checkin_books.short_description = 'Check In Selected Books'


#bookadmin controls look and feel of the book table, adjusts search fields, what is displayed, and separate filters for the books
class bookadmin(admin.ModelAdmin):
    search_fields = ['book_title', 'book_author', 'book_id']
    list_display = ["book_title", "book_author", "book_id", "in_library", "user", "calc_days_until_due",  "book_due_date", "calc_fine"]
    list_filter = ["in_library", "book_author", "user_id"]
    #Registers action from above, checkin_books
    actions = [checkin_books]
    class Meta:
        model = booktable2


#useradmin controls look and feel of the user table, adjusts search fields, what is displayed, and separate 'filters' for the users
class useradmin(admin.ModelAdmin):
    search_fields = ['user_first_name', 'user_last_name', 'user_id']
    list_display = ["user_last_name", "user_first_name", "user_id", "user_type"]
    list_filter = ["user_type"]

    class Meta:
        model = usertable2





#Registers the models into the admin pages, as well as their controlling admins of the models
admin.site.register(usertable2, useradmin)
admin.site.register(booktable2, bookadmin)



# -*- encoding: utf-8 -*-
"""
    flask_triangle.widgets.uibootstrap.typeahead
    --------------------------------------------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

from ..standard import TextInput


class Typeahead(TextInput):
    """
    This directive can be used to quickly create elegant typeheads with any form
    text input.
    """

    def __customize__(self, options, editable=True, required_length=1,
                      wait=0):
        self.html_attributes['data-typeahead'] = options
        if not editable:
            self.html_attributes['data-typeahead-editable'] = False
        if required_length > 1:
            self.html_attributes['data-typeahead-min-length'] = required_length
        if wait > 0:
            self.html_attributes['data-typeahead-wait-ms'] = wait

# -*- encoding: utf-8 -*-
"""
    flask_triangle.widgets.uibootstrap.datepicker
    ---------------------------------------------

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals


from flask_triangle.widget import Widget
import flask_triangle.modifiers


class Datepicker(Widget):
    """
    This directive can be used to quickly create elegant typeheads with any form
    text input.
    """

    schema = {'type': 'string'}

    @property
    def html_template(self):

        if self.popup:
            return '<input {{widget.html_attributes}}></input>'
        else:
            return '<div {{widget.html_attributes}}></div>'

    def __customize__(self, popup=False, date_format='fullDate', min=None,
                      max=None, required=False):

        self.popup = popup

        if self.popup:
            self.html_attributes['data-datepicker-popup'] = date_format
            self.html_attributes['type'] = 'text'

            # popup control
            if type(popup) is bool:
                state = self.bind.replace('.', '_')
                self.html_attributes['data-ng-init'] = \
                    '_{} = false;'.format(state)
                self.html_attributes['data-ng-click'] = \
                    '_{} != _{};'.format(state, state)
                self.html_attributes['data-is-open'] = '_{}'.format(state)
            else:
                self.html_attributes['data-ng-click'] = \
                    '{} != {};'.format(popup, popup)
                self.html_attributes['data-is-open'] = '{}'.format(popup)

        else:
            self.html_attributes['data-datepicker'] = None

        if min is not None:
            self.html_attributes['data-min'] = min
        if max is not None:
            self.html_attributes['data-max'] = max

        if required is not False:
            self.modifiers.append(flask_triangle.modifiers.Required(required))

        # server-side validation of sent data (ISO-8601)
        regex = ('^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[0-1]|0[1-9]'
                 '|[1-2][0-9])T(2[0-3]|[0-1][0-9]):([0-5][0-9]):([0-5][0-9])('
                 '\.[0-9]+)?(Z|[+-](?:2[0-3]|[0-1][0-9]):[0-5][0-9])?$')
        self.modifiers.append(flask_triangle.modifiers.Regexp(regex, False))

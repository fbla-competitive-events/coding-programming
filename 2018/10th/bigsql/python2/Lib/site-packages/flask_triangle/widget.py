# -*- encoding: utf-8 -*-
"""
    flask_triangle.widget
    ---------------------

    Base components and features of every widgets.

    :copyright: (c) 2013 by Morgan Delahaye-Prat.
    :license: BSD, see LICENSE for more details.
"""


from __future__ import absolute_import
from __future__ import unicode_literals

import re, inspect
from jinja2 import Template
from flask_triangle.helpers import UnicodeMixin, HTMLString
from flask_triangle.schema import Schema


class HtmlAttr(dict, UnicodeMixin):
    """
    Implements an object to manage and render attributes of an HTML element.

    HtmlAttr is a dict with a custom rendering to string method. It converts
    camelcase inputs to dash separated notation and removes the value of all
    boolean attributes.

        >>> demo = HtmlAttr(attr='value', boolean=None, camelCase=True)
        >>> print demo
        attr="value" boolean camel-case="true"
    """

    @staticmethod
    def _get_name(string):
        """
        Convert a string to a valid HTML attribute name.
        """
        if re.match(r'^[A-Za-z0-9]+$', string):
            words = re.split(r'(^[a-z]*)|([A-Z][^A-Z]+)', string)
            return '-'.join(c for c in words if c is not None and c).lower()
        return string.lower()

    @staticmethod
    def _get_value(value):
        """
        A value is converted to its string representation. For compatibility
        purpose with Javascript, `True` and `False` are in lower case.
        """
        string = '"{}"'

        if type(value) is bool:
            return string.format(unicode(value).lower())

        value = unicode(value)

        if value.endswith('|angular'):
            value = value[:-8]
            string = '"{{{{{{{{{}}}}}}}}}"'

        return string.format(value)

    @staticmethod
    def _to_attr(key, value):
        """
        Convert a key value pair to an attribute representation. If value is
        None, the attribute is considered as a boolean attribute present but
        with no value.

        :arg key: A string. The attribute name.
        :arg value: Anything. The value of the attribute.
        :return: A string.
        """
        # anticipated return (no need to process the value)
        if value is None:
            return HtmlAttr._get_name(key)

        return '{name}={value}'.format(name=HtmlAttr._get_name(key),
                                         value=HtmlAttr._get_value(value))

    def __unicode__(self):
        """
        Return all the attributes of a string.
        """
        unique = dict((self._get_name(k), v) for k, v in self.iteritems())
        return ' '.join(self._to_attr(k, v) for k, v in sorted(unique.items()))


class Widget(object):
    """
    The cornerstone of Flask-Triangle, the class
    :class:`~flask.triangle.widgets.base.Widget` is the base class of any
    widget.

    It implements the features for the schema building and HTML rendering of
    the widget.

    Defining a new widget
    ---------------------

    To create a new widget, inherit the :class:`Widget` in a new class.

        >>> class MyWidget(Widget):
        >>>     pass

    If the data held by the widget are required to be validated, add a class
    attribute `schema` with the validating JSON schema. This schema mustn't be
    aware of the user-defined binding (`bind` init's argument). On a simple
    widget, this `schema` will only be a JSON Schema's `property`. But on
    a complex widget, this can be a complete sub-schema. Here's a simple
    example :

        >>> class MyWidget(Widget):

        >>>     schema = {'type': 'integer',
        >>>               'minimum': 0,
        >>>               'maximum': 512}

    If the `schema` attribute is `None`, the JSON schema validation is
    disabled. This is the default behaviour.
    See jsonschema_ for more informations.

    If the widget requires additional arguments for the initialization, you can
    overload the `__init__` method as you would normally do, but you can also
    add a `__customize__` method. This method is automatically called when
    the instantiation process takes place and all its arguments are salvaged
    from the `__init__`'s `kwargs` arguments.

        >>> class MyWidget(Widget):

        >>>     schema = {'type': 'integer',
        >>>               'minimum': 0,
        >>>               'maximum': 512}

        >>>     def __customize__(self, custom_prop):
        >>>         self.custom_prop = custom_prop

    Beware ! Every `__customize__` methods in the inheritance tree of a widget
    will be called.

    To render a widget in HTML, you must define its HTML template. It consists
    in a string stored in the `html_template` class attribute. This string
    supports all the features of Jinja2_ and the instance of the widget is
    fully accessible from it.

        >>> class MyWidget(Widget):

        >>>     html_template = '''
        >>>     <mywidget {{widget.html_attributes}}>
        >>>         {{widget.custom_prop}}
        >>>     </mywidget>
        >>>     '''

        >>>     schema = {'type': 'integer',
        >>>               'minimum': 0,
        >>>               'maximum': 512}

        >>>     def __customize__(self, custom_prop):
        >>>         self.custom_prop = custom_prop

    `widget.html_attributes` automatically renders to a valid syntax for html
    attributes. The availability of advanced features in Jinja2_ let you create
    complex widget for your users. See Jinja2_ for more informations.

    .. _jsonschema: https://pypi.python.org/pypi/jsonschema
    .. _Jinja2: http://jinja.pocoo.org/

    """

    # the validation is not active by default.
    schema = None

    # the HTML template
    html_template = ('<em>'
                     'This Flask-Triangle widget is not renderable.'
                     '</em>')

    # the instance counter is used to keep track of the widget order in a form.
    instance_counter = 0

    # Properties
    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, value):
        self._name = value
        if value is not None:
            self.html_attributes['name'] = value
        else:
            self.html_attributes.pop('name', None)

    @property
    def bind(self):
        return self._bind

    @bind.setter
    def bind(self, value):
        self._bind = value
        self.html_attributes['data-ng-model'] = value

    @property
    def label(self):
        if self._label is None:
            return self.name
        return self._label

    @label.setter
    def label(self, value):
        self._label = value

    def __init__(self, bind, name=None,
                 label=None, description=None,
                 modifiers=None, html_attributes=None, **kwargs):
        """
        :arg bind: Assignable ``angular expression`` to data-bind to.
        See ngModel_ directive for more information.

        :arg name: String (*Optional*). Property name of the form under which
        the control is published.

        :arg id: String (*Optional*). Unique identifier of the control in the
        DOM.

        :arg label: String (*Optional*). Set the property of the widget used to
        get its label. If not set, the property will return the name of it.

        :arg description: String (*Optional*). Set the optional description of
        the widget.

        :arg modifiers: A `list` of `Modifiers` (*Optional*). The modifiers are
        the transformations the user might want to apply to a specific widget.

        :arg html_attributes: A `dict` (*Optional*). The key-value pairs in the
        dictionnary is converted to HTML attributes.

        .. _ngModel: http://docs.angularjs.org/api/ng.directive:ngModel
        """

        # increment the instance counter
        self.instance_counter = Widget.instance_counter
        Widget.instance_counter += 1

        self.modifiers = []
        self.html_attributes = HtmlAttr()

        self.bind = bind
        self.name = name
        self._label = label # see the property for the behavior of the label
        self.description = description

        # apply customizations before instance specific settings
        self._apply_customize(**kwargs)

        # set the optional attributes
        if html_attributes is not None:
            self.html_attributes.update(html_attributes)

        # set modifiers
        if modifiers is not None:
            self.modifiers += modifiers

        if self.__class__.schema is not None:
            self.schema = self._schema()

    # private methods
    def _apply_customize(self, **kwargs):
        """
        Walk the inheritance tree of the widget until reaching the `Widget`
        parent class and apply for each level the `__customize__` method if it
        exists.

        If the `__customize__` method requires argument, they're salvaged from
        the `kwargs`.
        """
        def recursive_apply(cls):
            """The recursion."""

            # the final condition
            if cls.__base__ != Widget:
                recursive_apply(cls.__base__)

            customize = getattr(cls, '__customize__', None)
            if customize is not None:
                args = inspect.getargspec(customize).args[1:]
                if not args:
                    customize(self)
                else:
                    local_kwargs = dict((k, v) for k, v in kwargs.iteritems()
                                                if k in args)
                    customize(self, **local_kwargs)
        # start the recursion
        if self.__class__ is not Widget:
            recursive_apply(self.__class__)

    def _init_schema(self, schema, fqn):
        """
        Combine the information from the `schema` attribute of the class and
        from `bind` property of the `Widget` instance to compute a validating
        JSON schema.
        """

        schema.update(Schema({'type': 'object', 'properties': {}}))

        # convert a nested object to nested schemas.
        parent = schema
        for child in self.bind.split('.')[:-1]:
            new = Schema({'type': 'object', 'properties': {}})
            parent['properties'][child] = new
            parent = new

        last = self.bind.split('.')[-1]
        parent['properties'][last] = Schema(self.__class__.schema)
        return True

    def _schema(self):
        """
        On the fly schema computation
        """
        schema = Schema()
        schema.apply_func(self._init_schema)
        # Apply modifiers
        for modifier in self.modifiers:
            if hasattr(modifier, 'alter_schema'):
                schema.apply_func(modifier.alter_schema)
        return schema.compile()

    def __call__(self, **kwargs):
        """
        Generate the HTML code of the current widget. Keyword arguments are
        used to format the generated HTML.

            >>> a = DemoWidget('hello.world', '{name}')
            >>> a(name='demo')
            '<input name="demo" ng-model="hello.world"/>
        """
        for modifier in self.modifiers:
            if hasattr(modifier, 'attributes'):
                self.html_attributes.update(modifier.attributes)
        template = Template(self.html_template).render(widget=self)
        return HTMLString(template.strip().format(**kwargs))

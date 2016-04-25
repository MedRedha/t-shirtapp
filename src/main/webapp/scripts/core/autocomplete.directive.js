'use strict';

angular.module('app')
    .directive('autoComplete', function($timeout) {
        return function(scope, elem, attr) {
            elem.autocomplete({
                source: scope[attr.uiItems],
                select: function() {
                    $timeout(function() {
                        elem.trigger('input');
                    }, 0);
                }
            });
        };
    })
    ;

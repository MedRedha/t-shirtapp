/**
 * Created by Amine on 12/24/15.
 */

'use strict';

angular.module('app')
    .factory('Logo', function ($resource,DateUtils) {
        return $resource('api/logo/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);

                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {

                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            }
        });
    })

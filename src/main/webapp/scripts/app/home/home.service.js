/**
 * Created by Amine on 12/24/15.
 */

'use strict';

angular.module('app')
    .factory('Home', function ($resource,DateUtils) {
        return $resource('api/dashboard/:id', {}, {
            'query': {method: 'GET'},
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

/**
 * Created by Amine on 12/24/15.
 */

'use strict';

angular.module('app')
    .factory('Client', function ($resource,DateUtils) {
        return $resource('api/client/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdDate = DateUtils.convertLocaleDateFromServer(data.createdDate);
                    data.lastModifiedDate = DateUtils.convertDateTimeFromServer(data.lastModifiedDate);

                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {

                    return angular.toJson(data);
                }
            }
        });
    })

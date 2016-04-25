'use strict';

angular.module('app')
    .factory('AuthServerProvider', function loginService($http, localStorageService, $window) {
        return {
            login: function(credentials) {
                var data = 'username=' + encodeURIComponent(credentials.username) +
                    '&password=' + encodeURIComponent(credentials.password) +'&submit=Login';
                return $http.post('api/authentication', data, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }).success(function (response) {
                    return response;
                });
            },
            logout: function() {
                // logout from the server
                $http.post('/logout').success(function (response) {
                    localStorageService.clearAll();
                    // to get a new csrf token call the api
                    return response;
                });
            }
        };
    });

'use strict';

angular.module('app')
    .run(function ($rootScope, $location, $window, $http, $state,  Auth, Principal) {


        })
    .factory('authExpiredInterceptor', function ($rootScope, $q, $injector, localStorageService) {
        return {
            responseError: function(response) {
                // If we have an unauthorized request we redirect to the login page
                // Don't do this check on the account API to avoid infinite loop
                if (response.status == 401 ){
                    var Auth = $injector.get('Auth');
                    var $state = $injector.get('$state');
                    Auth.logout();
                    $state.go('login');
                }
                return $q.reject(response);
            }
        };

    });

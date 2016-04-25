'use strict';

angular.module('app')
    .factory('Auth', function Auth($rootScope, $state, $q, Principal, AuthServerProvider) {
        return {
            login: function (credentials, callback) {
                var cb = callback || angular.noop;
                var deferred = $q.defer();

                AuthServerProvider.login(credentials).then(function (data) {
                    // retrieve the logged account information
                    Principal.identity(true).then(function(account) {
                        deferred.resolve(data);
                    });
                    return cb();
                }).catch(function (err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

                return deferred.promise;
            },
            authorize: function(force) {
                console.log("authorize");
                return Principal.identity(force)
                    .then(function() {
                        var isAuthenticated = Principal.isAuthenticated();

                        if ($rootScope.toState.data.authorities && $rootScope.toState.data.authorities.length > 0 && !Principal.hasAnyAuthority($rootScope.toState.data.authorities)) {


                            if (isAuthenticated) $state.go('accessdenied'); // user is signed in but not authorized for desired state
                            else {
                                // user is not authenticated. stow the state they wanted before you
                                // send them to the signin state, so you can return them when you're done
                                $rootScope.returnToState = $rootScope.toState;
                                $rootScope.returnToStateParams = $rootScope.toStateParams;

                                // now, send them to the signin state so they can log in
                                $state.go('login');
                            }
                        }

                    });
            },

            logout: function () {
                AuthServerProvider.logout();
                Principal.authenticate(null);
                // Reset state memory
                $rootScope.previousStateName = undefined;
                $rootScope.previousStateNameParams = undefined;
            }
        };
    });

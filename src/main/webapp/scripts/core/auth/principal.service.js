'use strict';

angular.module('app')
    .factory('Principal', function Principal($q, Account) {

        var _identity,
            _authenticated = false;

        return {
            isIdentityResolved: function () {
                return angular.isDefined(_identity);
            },
            isAuthenticated: function () {
                return _authenticated;
            },
            hasAuthority: function (authority) {
                if (!_authenticated) {
                    return $q.when(false);
                }

                return this.identity().then(function(_id) {
                    return _id.authorities && _id.authorities.indexOf(authority) !== -1;
                }, function(err){
                    return false;
                });
            },
            hasAnyAuthority: function (authorities) {

                if (!_authenticated || !_identity || !_identity.authorities) {

                    return false;
                }

                for (var i = 0; i < authorities.length; i++) {

                    for (var j = 0; j < _identity.authorities.length; j++) {
                        console.log("autho--->"+_identity.authorities[j].name)
                        if (_identity.authorities[j].name==authorities[i]) {
                            return true;
                        }
                    }
                }

                return false;
            },
            authenticate: function (identity) {
                _identity = identity;
                _authenticated = identity !== null;
            },
            identity: function (force) {
                var deferred = $q.defer();

                if (force === true) {
                    _identity = undefined;
                }


                // retrieve the identity data from the server, update the identity object, and then resolve.
                Account.get().$promise
                    .then(function (account) {
                        console.log( account.data)
                        _identity = account.data;
                        _authenticated = true;
                        deferred.resolve(_identity);
                    })
                    .catch(function() {
                        _identity = null;
                        _authenticated = false;
                        deferred.resolve(_identity);
                    });
                return deferred.promise;
            }
        };
    });

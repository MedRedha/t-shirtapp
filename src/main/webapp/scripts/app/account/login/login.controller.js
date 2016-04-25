'use strict';

angular.module('app')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth) {
        $scope.user = {};
        $scope.errors = {};


        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                username: $scope.username,
                password: $scope.password
            }).then(function () {
                $scope.authenticationError = false;

                    $state.go('home');

            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });

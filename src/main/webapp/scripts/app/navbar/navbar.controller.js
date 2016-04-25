/**
 * Created by Amine on 12/22/15.
 */


    'use strict';

    angular.module('app')
        .controller('NavbarController',

    function($scope, $location, $state, Auth, Principal){

        $scope.isAuthenticated =Principal.isAuthenticated() ;

        $scope.logout = function () {
            Auth.logout();
            $state.go('login');
        };
    });


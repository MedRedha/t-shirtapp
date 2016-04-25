/**
 * Created by Amine on 12/22/15.
 */


    'use strict';

    angular.module('app')
        .controller('SideNavbarController',

    function($scope, $location, $state, Auth, Principal){
        console.log("ddddd");

        $scope.isAuthenticated =Principal.isAuthenticated() ;

    });


/**
 * Created by Amine on 12/22/15.
 */
'use strict';

angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                url: '/',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: "scripts/app/home/home.html",
                        controller: "HomeController",
                        controllerAs: "vm"
                    }
                }

            });


    });
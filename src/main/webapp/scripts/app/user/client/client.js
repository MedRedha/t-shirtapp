'use strict';


angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('client', {
                url: '/clients',
                data: {
                    authorities: ['ROLE_USER']
                },
                parent: 'site',
                views: {
                    'content@': {
                        templateUrl: "scripts/app/user/client/client.html",
                        controller: "ClientController",
                        controllerAs: "vm"
                    }
                }

            })
            .state('client.create', {
                parent: "client",
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/user/client/clientDialog.html',
                        controller: "ClientDialogController",
                        controllerAs: "vm"
                    }
                }



            }).state('client.detail', {
                url: '/client/{id}',
            data: {
                authorities: ['ROLE_USER']
            },
                parent: 'client',
                views: {
                    'content@': {
                        templateUrl: "scripts/app/user/client/clientDetails.html",
                        controller: "ClientDetailsController",
                        controllerAs: "vm"
                    }
                }


            })


    });
    
    
    
    

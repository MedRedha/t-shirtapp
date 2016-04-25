'use strict';


angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userweb', {
                url: '/users',
                parent: 'site',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: "scripts/app/user/web/user.html",
                        controller: "UserController",
                        controllerAs: "vm"
                    }
                }

            }).state('userweb.detail', {
                url: '/user/{id}',
            data: {
                authorities: ['ROLE_ADMIN']
            },
                views: {
                    'content@': {
                        templateUrl: "scripts/app/user/web/userDetails.html",
                        controller: "UserDetailsController",
                        controllerAs: "vm"
                    }
                }


            }).state('userweb.create', {
                parent: "userweb",
                url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/user/web/userDialog.html',
                        controller: "UserDialogController",
                        controllerAs: "vm"
                    }
                }



            }).state('userweb.edit', {
            parent: "userweb",
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/user/web/userDialog.html',
                    controller: "UserDialogController",
                    controllerAs: "vm"
                }
            }

        });

    });
    
    
    
    

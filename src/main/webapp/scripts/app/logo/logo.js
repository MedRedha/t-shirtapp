'use strict';


angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('logo', {
                url: '/logos',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: "scripts/app/logo/logo.html",
                        controller: "LogoController",
                        controllerAs: "vm",
                    }
                }

            }).state('logo.create', {
                parent: "logo",
                url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },

                views: {
                    'content@': {
                        templateUrl: 'scripts/app/logo/logoDialog.html',
                        controller: "LogoDialogController",
                        controllerAs: "vm"
                    }
                }



            }).state('logo.edit', {
            parent: "logo",
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/logo/logoDialog.html',
                    controller: "LogoDialogController",
                    controllerAs: "vm"
                }
            }

        });

    });
    
    
    
    

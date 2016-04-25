'use strict';


angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('textColor', {
                url: '/textColors',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: "scripts/app/textColor/textColor.html",
                        controller: "TextColorController",
                        controllerAs: "vm",
                    }
                }

            }).state('textColor.create', {
                parent: "textColor",
                url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },

                views: {
                    'content@': {
                        templateUrl: 'scripts/app/textColor/textColorDialog.html',
                        controller: "TextColorDialogController",
                        controllerAs: "vm"
                    }
                }



            }).state('textColor.edit', {
            parent: "textColor",
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/textColor/textColorDialog.html',
                    controller: "TextColorDialogController",
                    controllerAs: "vm"
                }
            }

        });

    });
    
    
    
    

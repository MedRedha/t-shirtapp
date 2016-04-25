/**
 * Created by Amine on 12/25/15.
 */


(function(){
    angular.module("app")
        .config(routeConfig);
    routeConfig.$inject=['$stateProvider']
    function routeConfig($stateProvider){

        $stateProvider.state('api',{
            url : '/api',
            parent: 'site',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'API'
            },
            views: {
                'content@': {
                    templateUrl: "scripts/app/api/api.html"
                }
            }
        })

    }
})();
/**
 * Created by Amine on 12/26/15.
 */
(function(){
'use strict'
    angular.module("app")
        .controller("UserDetailsController",clientDetailsController);

    clientDetailsController.$inject=['User', 'DateUtils','$stateParams']

    function clientDetailsController(User, DateUtils,$stateParams){


        var vm =this;
        vm.user = {};
        vm.load = function (id) {
            User.get({id: id}, function(result) {
                vm.user = result;

            });
        };

        vm.load($stateParams.id);



    }

})();
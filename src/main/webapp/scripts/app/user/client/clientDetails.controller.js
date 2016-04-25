/**
 * Created by Amine on 12/26/15.
 */
(function(){

    angular.module("app")
        .controller("ClientDetailsController",clientDetailsController);

    clientDetailsController.$inject=['Client', 'DateUtils','$stateParams']

    function clientDetailsController(Client, DateUtils,$stateParams){


        var vm =this;
        vm.client = {};
        vm.load = function (id) {
            Client.get({id: id}, function(result) {
                vm.client = result;
                console.log("amama")
            });
        };

        vm.load($stateParams.id);
        


    }

})();
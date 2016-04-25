/**
 * Created by Amine on 12/22/15.
 */


/**
 * Created by Amine on 12/24/15.
 */

(function () {
    angular.module('app')
        .controller('HomeController', homeController);
    homeController.$injector = ['Home']
    function homeController(Home) {
        var vm = this;


        vm.loadAll = function () {
            Home.query(function (result) {
                vm.home = result;
            });
        };


        vm.loadAll();


    }
})();

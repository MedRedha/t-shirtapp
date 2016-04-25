/**
 * Created by Amine on 12/26/15.
 */


(function(){
    'use strict';
    angular.module("app")
        .controller("OrderUpdateController",OrderCreateController);

    OrderCreateController.$inject=['Order','$stateParams','$http','$state','Client','User'];

    function OrderCreateController(Order,$stateParams,$http,$state,Client,User){

        var vm = this;




        vm.order={};
        vm.temp={};
        vm.alerts=[];
        vm.type={};
        vm.size={};
        vm.product={};
        vm.showSize=false;
        vm.showColor=false;
        vm.tempClients=[];


        if($stateParams.id!=null) {
            vm.loadOrder = function () {
                Order.get({id: $stateParams.id}, function (result) {
                    vm.order = result;
                    vm.showSize=true;
                    vm.showColor=true;

                });
            }
            vm.loadOrder()
        }
        vm.productType=(function(){

            $http({
                method: 'GET',
                url: '/api/productType/status',
                params:{'status':'ENABLED'}
            }).then(function successCallback(response) {
                vm.temp=response.data;

            }, function errorCallback(response) {

            });
            return vm.temp;
        })();
        vm.loadAllUsers = function () {
            User.query(function (result) {
                vm.tempUsers = result;
            });
        };

        vm.loadAllUsers();

        vm.save = function () {
            vm.isSaving = true;


                if (vm.order.id != null) {
                    Order.update(vm.order,function(data,headers){
                        $state.go('order');
                    },function(httpResponse){
                        console.log(httpResponse.headers('failure'));
                        vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                    });
                } else {
                    Order.save(vm.order,function(data,headers){
                        $state.go('order');
                    },function(httpResponse){
                        console.log(httpResponse.headers('failure'));
                        vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                    });

                }

        };







        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };
    }

})();
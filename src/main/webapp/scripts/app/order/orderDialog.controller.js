/**
 * Created by Amine on 12/26/15.
 */


(function () {
    'use strict';
    angular.module("app")
        .controller("OrderDialogController", orderDialogController);

    orderDialogController.$inject = ['Order', '$stateParams', '$http', '$state', 'SweetAlert','Lightbox'];

    function orderDialogController(Order, $stateParams, $http, $state, SweetAlert,Lightbox) {

        var vm = this;


        vm.order = {};
        vm.temp = {};
        vm.alerts = [];
        vm.images =[{
            'url': '1.jpg'
        }]

        if ($stateParams.id != null) {
            vm.loadOrder = function () {
                Order.get({id: $stateParams.id}, function (result) {
                    vm.order = result;

                    vm.images[0].url="api/order/payment?orderID="+vm.order.id

                });
            };
            vm.loadOrder();
        }


        vm.getExt = function (fileName) {
            return fileName.substr(fileName.lastIndexOf('.'))
        }


        vm.screens = (function () {

            $http({
                method: 'GET',
                url: '/api/order/' + $stateParams.id + '/screens'
            }).then(function successCallback(response) {
                vm.tempList = response.data;
            }, function errorCallback(response) {

            });

        })();


        vm.geImageSide = function (name) {
            if (name[0] == 0 || name == 0)
                return "FRONT";
            if (name[0] == 1 || name == 1)
                return "BACK";
            if (name[0] == 2 || name == 2)
                return "RIGHT";
            if (name[0] == 3 || name == 3)
                return "LEFT";
        }


        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };

        vm.statusNew = function (status) {

            return status == "NEW";
        }

        vm.statusCanceled = function (status) {

            return status == "CANCELED";
        }


        vm.statusProgress = function (status) {

            return status == "PROGRESS";
        }

        vm.statusDelivered = function (status) {

            return status == "DELIVERED";
        }

        vm.statusOUT = function (status) {

            return status == "OUT";
        }

        vm.statusWaitPayment = function (status) {

            return status == "WAIT_PAYMENT";
        }


        vm.payment = function (payment) {


            if(payment==null)
                return false;
            else
                return true;
        }


        vm.validate = function (order, status) {

            $http({
                method: 'PUT',
                url: '/api/order/' + order.id + "/" + status + "?canceledFor=",
            }).then(function successCallback(response) {
                vm.order = response.data;
                vm.alerts.push({type: 'success', msg: 'Order ' + vm.order.status});

            }, function errorCallback(response) {
                vm.alerts.push({type: 'danger', msg: '' + response.headers('failure')});
            });
        }
        vm.cancele = function (order, status) {




                SweetAlert.swal({
                    title: "An input!",
                    text: "Write something interesting:",
                    type: "input",
                    showCancelButton: true,
                    closeOnConfirm: true,
                    animation: "slide-from-top",
                    inputPlaceholder: "Write something"
                }, function (inputValue) {
                    if (inputValue === false) return false;
                    if (inputValue === "") {
                        swal.showInputError("You need to write something!");
                        return false
                    }
                    $http({
                        method: 'PUT',
                        url: '/api/order/' + order.id + "/" + status + "?canceledFor="+inputValue,
                    }).then(function successCallback(response) {
                        vm.order = response.data;
                        vm.alerts.push({type: 'success', msg: 'Order ' + vm.order.status});
                        return false;

                    }, function errorCallback(response) {
                        vm.alerts.push({type: 'danger', msg: '' + response.headers('failure')});
                        return false;
                    });
                });




        };


        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };

        vm.openLightboxModal = function () {
            Lightbox.openModal(vm.images, 0);
        };

    }

})();
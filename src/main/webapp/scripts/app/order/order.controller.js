/**
 * Created by Amine on 12/24/15.
 */

(function () {
    angular.module('app')
        .controller('OrderController', orderController);
    orderController.$injector = ['Order','Client', 'SweetAlert', 'ParseLinks','$http']
    function orderController(Order,Client, SweetAlert, ParseLinks,$http) {

        var vm = this;
        vm.orders = [];
        vm.alerts = [];
        vm.statusFilter = {'NEW':true,'PROGRESS':true,'OUT':true,'WAIT_PAYMENT':true,'CANCELED':false,'DELIVERED':false};
        vm.currentPage = 1;

        vm.loadAll = function () {
            Order.query({page: vm.currentPage - 1, size: 5,status:vm.statusFilter}, function (result, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.orders = result;
            });
        };
        vm.ItemPerPage = 5;
        vm.loadAll();
        vm.pageChanged = function () {
            vm.loadAll()
        };




        vm.disable = function (item) {

            SweetAlert.swal({
                    title: "Are you sure?",
                    text: "Your will not be able to use this order",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, Disable it!"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        item.status = "DISABLED"
                        Product.update(item, function (data, headers) {

                            vm.alerts.push({
                                type: 'success',
                                msg: 'Product ' + data.ref + " : Status changed to " + data.status
                            });

                        }, function (httpResponse) {
                            console.log(httpResponse.headers('failure'));
                            vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
                        })

                    }
                });

        }


        vm.enable = function (item) {
            item.status = "ENABLED"
            Product.update(item, function (data) {
                vm.alerts.push({type: 'success', msg: 'Product ' + data.ref + " : Status changed to " + data.status});
            }, function (httpResponse) {
                console.log(httpResponse.headers('failure'));
                vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
            })
        }

        vm.export = function (item) {
            item.status = "ENABLED"
            Product.update(item, function (data) {
                vm.alerts.push({type: 'success', msg: 'Product ' + data.ref + " : Status changed to " + data.status});
            }, function (httpResponse) {
                console.log(httpResponse.headers('failure'));
                vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
            })
        }

        vm.isDisable = function (status) {
            return status === "DISABLED";
        }


        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };

        vm.statusProgress = function (status) {

            return status == "PROGRESS";
        }

        vm.statusDelivered = function (status) {

            return status == "DELIVERED";
        }

    }
})();

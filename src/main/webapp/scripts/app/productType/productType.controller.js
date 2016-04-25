/**
 * Created by Amine on 12/24/15.
 */

(function () {
    angular.module('app')
        .controller('ProductTypeController', productTypeController);
    productTypeController.$injector = ['ProductType','SweetAlert']
    function productTypeController(ProductType,SweetAlert) {
        var vm = this;
        vm.productTypes = [];
        vm.alerts = [];


        vm.loadAll = function () {
            ProductType.query(function (result) {
                vm.productTypes = result;
            });
        };


        vm.loadAll();

        vm.disable=function(item){
            SweetAlert.swal({
                    title: "Are you sure?",
                    text: "Your will not be able to use this Category",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, Disable it!"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        item.status="DISABLED"
                        ProductType.update(item,function (data, headers) {

                            vm.alerts.push({
                                type: 'success',
                                msg: 'ProductType ' + data.ref + " : Status changed to " + data.status
                            });

                        }, function (httpResponse) {
                            console.log(httpResponse.headers('failure'));
                            vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
                        })

                    }
                });


        }

        vm.enable=function(item){
            item.status="ENABLED"
            ProductType.update(item,function (data, headers) {

                vm.alerts.push({
                    type: 'success',
                    msg: 'ProductType ' + data.ref + " : Status changed to " + data.status
                });

            }, function (httpResponse) {
                console.log(httpResponse.headers('failure'));
                vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
            })
        }

        vm.isDisable=function(status){
            return status==="DISABLED";
        }



        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };

    }
})();

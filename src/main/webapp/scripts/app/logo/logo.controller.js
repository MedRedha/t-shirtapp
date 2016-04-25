/**
 * Created by Amine on 12/24/15.
 */

(function () {
    angular.module('app')
        .controller('LogoController', logoController);
    logoController.$injector = ['Logo', 'SweetAlert', 'ParseLinks']
    function logoController(Logo, SweetAlert, ParseLinks) {

        var vm = this;
        vm.logos = [];
        vm.alerts = [];
        vm.currentPage = 1;

        vm.loadAll = function () {
            Logo.query({page: vm.currentPage - 1, size: 5}, function (result, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.logos = result;
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
                    text: "Your will not be able to use this logo",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, Disable it!"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        item.status = "DISABLED"
                        Logo.update(item, function (data, headers) {

                            vm.alerts.push({
                                type: 'success',
                                msg: 'Logo ' + data.ref + " : Status changed to " + data.status
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
            Logo.update(item, function (data) {
                vm.alerts.push({type: 'success', msg: 'Logo ' + data.ref + " : Status changed to " + data.status});
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



        vm.delete=function(i) {
            SweetAlert.swal({
                    title: "Are you sure?",
                    text: "The wont able to recover the Logo: "+ i.tag +" ",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, delete it"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        Logo.delete({id: i.id},
                            function () {
                                vm.loadAll();
                                vm.alerts.push({type: 'success', msg: 'Logo Deleted'});

                            });

                    }
                });

        }

    }
})();

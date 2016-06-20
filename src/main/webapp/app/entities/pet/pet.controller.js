(function() {
    'use strict';

    angular
        .module('petStoreApp')
        .controller('PetController', PetController);

    PetController.$inject = ['$scope', '$state', '$log', 'Pet', 'ParseLinks', 'AlertService', 'Principal'];

    function PetController ($scope, $state, $log, Pet, ParseLinks, AlertService, Principal) {
        var vm = this;

        vm.pets = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();

        function loadAll () {
            Pet.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.pets.push(data[i]);
                }
                $log.log("Principal: " + JSON.stringify(Principal));
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.pets = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();

(function() {
    'use strict';
    angular
        .module('petStoreApp')
        .factory('Pet', Pet);

    Pet.$inject = ['$resource'];

    function Pet ($resource) {
        var resourceUrl =  'pet/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

import testAddEntities from "./test-add-entities.js";
import testGetEntities from "./test-get-entities.js";

export let options = {
    vus: 100, // Количество виртуальных пользователей
    duration: '5m', // Продолжительность теста
    thresholds: {
        'http_req_duration': ['p(99) < 500'],
        'http_reqs': ['rate<0.03']
    }
};

export default function () {
    testAddEntities();
    testGetEntities();
}
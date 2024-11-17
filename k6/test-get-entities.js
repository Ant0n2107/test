import {sleep} from 'k6';
import {HOST, get, SUCCESS_RESPONSE_CODES, DEFAULT_TRIGGER_TIME} from "./commons.js";

export function getStats() {
    return {
        'is get status OK': (r) => SUCCESS_RESPONSE_CODES.includes(r.status),
        'is get OK': (r) => r.body !== null,
        'is get response time less than 100ms': (r) => r.timings.duration < DEFAULT_TRIGGER_TIME
    };
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

export default function () {
    const page = getRandomInt(1, 100);
    const size = getRandomInt(1, 100);

    const params = {
        headers: {'Content-Type': 'application/json'},
        params: {
            page: page,
            size: size,
        },
    };

    get(HOST + '/search', params, getStats());

    sleep(5);
}
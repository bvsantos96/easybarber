import { create } from 'zustand';

interface ServiceTypeStore {
    serviceTypes: ICategory[];
    setServiceTypes: (serviceTypes: ICategory[]) => void;
    getServices: (serviceTypes: number[]) => ICategory[];
    getServiceType: (serviceType: number) => ICategory | undefined;
}

const useServiceTypeStore = create<ServiceTypeStore>(
    (set, get) => ({
        serviceTypes: [],
        setServiceTypes: (serviceTypes: ICategory[]) => set({ serviceTypes }),
        getServices: (serviceTypes: number[]) => {
            return get().serviceTypes.filter((serviceType) => serviceTypes.includes(serviceType.id));
        },
        getServiceType: (serviceType: number) => {
            return get().serviceTypes.find((st) => st.id === serviceType);
        }
    })
);

export default useServiceTypeStore;
